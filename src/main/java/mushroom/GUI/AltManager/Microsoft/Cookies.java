package mushroom.GUI.AltManager.Microsoft;

import com.google.gson.*;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import mushroom.GUI.AltManager.AltManagerGUI;
import mushroom.GUI.AltManager.SessionLogin.SessionChanger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class Cookies {

    public static String path = "";

    private String cookie;

    private String errorMessage;

    public static OkHttpClient client = (new OkHttpClient()).newBuilder().followRedirects(false).followSslRedirects(false).build();
    public void validateCookie(String cookie) {
        HttpGet request = new HttpGet("https://login.live.com/oauth20_authorize.srf?redirect_uri=https://sisu.xboxlive.com/connect/oauth/XboxLive&response_type=code&state=LAAAAAEB2OQKA4AphougPWXz6j8KrT_KxS1BLPFjAd7VgjpBw60PmPFt0diwZWZkMzMwZmY5ZjgzNDU2YWEzZTdiMWYyYmQ4NWNkMWMx&client_id=000000004420578E&scope=XboxLive.Signin&lw=1&fl=dob,easi2&xsup=1&cobrandid=8058f65d-ce06-4c30-9559-473c9275a65d");
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
        request.setHeader("Accept-Encoding", "gzip, deflate, br");
        request.setHeader("Accept-Language", "en-US,en;q=0.5");
        request.setHeader("Cache-Control", "no-cache");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Cookie", cookie);
        request.setHeader("Host", "login.live.com");
        request.setHeader("Pragma", "no-cache");
        request.setHeader("Sec-Fetch-Dest", "document");
        request.setHeader("Sec-Fetch-Mode", "navigate");
        request.setHeader("Sec-Fetch-Site", "none");
        request.setHeader("Sec-Fetch-User", "?1");
        request.setHeader("Upgrade-Insecure-Requests", "1");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/20100101 Firefox/105.0");
        try {
            HttpClients.createDefault().execute(request);
        } catch (IOException e) {
            AltManagerGUI.status = "§4failed to connect to website";
            //e.printStackTrace();
        }
    }

    public static void copyCookieToClipBoard() {
        try {
            new Cookies().authenticateAsync(jsonObject -> {
                String token = String.valueOf(jsonObject.get("token"));

                if (token != null && token.endsWith("\"")) {
                    StringSelection selectedText = new StringSelection(token.replaceAll("\"", ""));
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selectedText, selectedText);
                    AltManagerGUI.status = "§2session id copied to clipboard :) hf";
                }
            });
        } catch (Exception e) {
            AltManagerGUI.status = "§4cookie invalid, everything has gone wrong!!";
        }
    }


    public static void loginWithCookie() {
        try {

            new Cookies().authenticateAsync(jsonObject -> {
                String token = String.valueOf(jsonObject.get("token"));

                if (token != null && token.endsWith("\"")) {
                    token = token.replaceAll("\"", "");

                    SessionChanger.loginWSession(token);
                }
                else AltManagerGUI.status = "§4cookie invalid";
            });
        } catch (Exception e) {
            AltManagerGUI.status = "§4cookie invalid, everything has gone wrong!!";
        }
    }



    public String getCookiesFromFile(String path) {
        if (path.endsWith(".zip")) {
            try {
                unpack(path, new File(path.substring(0, path.lastIndexOf("\\")) + "\\MushroomCookies"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File file = new File(path);
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
        } catch (IOException iOException) {
            AltManagerGUI.status = "§4failed to read file";
        }


        bufferedReader.lines().forEach(line -> {
            stringBuilder.append(line);
            stringBuilder.append("; ");
            stringBuilder.append("\n");
        });

        try {
            bufferedReader.close();
        } catch (Exception ignored) {}

        return stringBuilder.toString();
    }

    public void authenticateAsync(Consumer<JsonObject> out) {
        cookie = formatCookies(getCookiesFromFile(path));
        validateCookie(cookie);

        ForkJoinPool.commonPool().execute(() -> {
            try {
                authenticate(out);
            } catch (IOException var3) {
                AltManagerGUI.status = "§4cookie invalid";
                var3.printStackTrace();
            }
        });
    }

    public void authenticateAsync(String cookie, Consumer<JsonObject> out) {
        this.cookie = cookie;
        ForkJoinPool.commonPool().execute(() -> {
            try {
                authenticate(out);
            } catch (IOException var3) {
                AltManagerGUI.status = "§4cookie invalid";
                var3.printStackTrace();
            }
        });
    }

    private void authenticate(Consumer<JsonObject> out) throws IOException {
        String redirectLocation = getRedirectLocation();
        String xboxUrl = getXboxUrl(redirectLocation);
        String encodedTokenData = getEncodedTokenData(xboxUrl);
        String minecraftAccessToken = authenticateMinecraft(encodedTokenData);
        if (checkLicense(minecraftAccessToken)) {
            out.accept(constructProfile(minecraftAccessToken));
        } else {
            AltManagerGUI.status = "§4doesn't own minecraft";
            this.errorMessage = "No game license";
        }
    }

    private JsonObject constructProfile(String accessToken) {
        try {
            Request request = (new Request.Builder()).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0").header("Authorization", "Bearer " + accessToken).url("https://api.minecraftservices.com/minecraft/profile").build();
            Response response = client.newCall(request).execute();
            JsonObject profile = new Gson().fromJson((Objects.requireNonNull(response.body())).string(), JsonObject.class).getAsJsonObject();
            JsonObject out = new JsonObject();
            out.addProperty("uuid", profile.get("id").getAsString());
            out.addProperty("username", profile.get("name").getAsString());
            out.addProperty("token", accessToken);
            return out;
        } catch (IOException e) {
            AltManagerGUI.status = "§4cookie invalid";
            throw new RuntimeException(e);
        }
    }

    private boolean checkLicense(String accessToken) throws IOException {
        Request request = (new Request.Builder()).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0").header("Authorization", "Bearer " + accessToken).url("https://api.minecraftservices.com/entitlements/license?requestId=checker").build();
        Response response = client.newCall(request).execute();
        boolean hasGames = false;
        for (JsonElement element : new Gson().fromJson((Objects.requireNonNull(response.body())).string(), JsonObject.class).get("items").getAsJsonArray()) {
            JsonObject jsonObject = element.getAsJsonObject();
            String source = jsonObject.get("source").getAsString();
            if (source.equals("PURCHASE") || source.equals("MC_PURCHASE")) {
                hasGames = true;
                break;
            }
            else {
                AltManagerGUI.status = "§4cookie doesn't own minecraft?";
            }
        }
        return hasGames;
    }

    private String authenticateMinecraft(String encodedData) throws IOException {
        JsonArray array = new Gson().fromJson(new String(Base64.getDecoder().decode(encodedData)), JsonArray.class);
        String userHash = array.get(1).getAsJsonObject().get("Item2").getAsJsonObject().get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();
        String tempToken = array.get(1).getAsJsonObject().get("Item2").getAsJsonObject().get("Token").getAsString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.format("{\n\t\"identityToken\": \"XBL3.0 x=%s;%s\",\n\t\"ensureLegacyEnabled\": \"True\"\n}", new Object[] { userHash, tempToken }));
        Request request = (new Request.Builder()).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0").url("https://api.minecraftservices.com/authentication/login_with_xbox").post(body).build();
        Response response = this.client.newCall(request).execute();
        JsonObject object = new Gson().fromJson((Objects.requireNonNull(response.body())).string(), JsonObject.class);
        if (!object.has("access_token")) {
            AltManagerGUI.status = "§4no microsoft tokens";
            throw new UnsupportedOperationException(this.errorMessage = "No microsoft access token present!");
        }
        return object.get("access_token").getAsString();
    }

    private String getEncodedTokenData(String xboxUrl) throws IOException {
        Request request = (new Request.Builder()).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0").header("Cookie", this.cookie).url(xboxUrl).build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401 && (Objects.<String>requireNonNull(response.header("WWW-Authenticate"))).contains("account_creation_required"))
            throw new UnsupportedOperationException(this.errorMessage = "Account creation required!");
        if (response.code() == 302 && (Objects.<String>requireNonNull(response.header("Location"))).contains("accessToken="))
            return (Objects.requireNonNull(response.header("Location"))).split("accessToken=")[1];

        AltManagerGUI.status = "§4no access token";
        throw new UnsupportedOperationException(this.errorMessage = "Access token is not present!");
    }

    private String getRedirectLocation() throws IOException {
        Request request = (new Request.Builder()).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0").header("Cookie", this.cookie).url("https://sisu.xboxlive.com/connect/XboxLive/?state=login&ru=https://www.minecraft.net/en-us/login").build();
        Response response = client.newCall(request).execute();
        if (response.code() == 302 && (Objects.<String>requireNonNull(response.header("Location"))).contains("oauth20_authorize.srf"))
            return response.header("Location");

        AltManagerGUI.status = "§4failed getting redirect";
        throw new UnsupportedOperationException(this.errorMessage = "Redirect location not found");
    }

    private String getXboxUrl(String location) throws IOException {
        Request request = (new Request.Builder()).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0").header("Cookie", this.cookie).url(location).build();
        Response response = client.newCall(request).execute();
        if (response.code() == 302 && (Objects.requireNonNull(response.header("Location"))).contains("code="))
            return response.header("Location");

        AltManagerGUI.status = "§4failed getting redirect";
        throw new UnsupportedOperationException(this.errorMessage = "No code/redirect present");
    }

    public String formatCookies(String fileContent) {
        StringBuilder cook = new StringBuilder();
        for (String s : fileContent.split("\n")) {
            String[] strings = s.split("\t");
            cook.append(strings[5]).append("=").append(strings[6]);
        }
        return cook.substring(0, cook.length() - 2);
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Cookies() {}


    public static void unpack(String sourceDirPath, File zipFilePath) throws IOException {
        if (zipFilePath.exists()) zipFilePath.delete();

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(sourceDirPath)));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(zipFilePath, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdir()) {
                    AltManagerGUI.status = "§4failed copying out files";
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdir()) {
                    AltManagerGUI.status = "§4failed copying out files";
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            AltManagerGUI.status = "§4failed copying out files";
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
