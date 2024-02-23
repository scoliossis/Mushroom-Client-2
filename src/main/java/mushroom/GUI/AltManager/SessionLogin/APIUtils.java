package mushroom.GUI.AltManager.SessionLogin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class APIUtils {
    public static String[] getProfileInfo(String token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://api.minecraftservices.com/minecraft/profile");
        request.setHeader("Authorization", "Bearer " + token);
        CloseableHttpResponse response = client.execute(request);
        String jsonString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        String IGN = jsonObject.get("name").getAsString();
        String UUID = jsonObject.get("id").getAsString();
        return new String[]{IGN, UUID};
    }
    public static String tokenFromMicrosoft(String code) throws IOException {
        String[] xstsAndHash = xstsAndHashFromMicrosoft(code);
        String xstsToken = xstsAndHash[0];
        String userHash = xstsAndHash[1];
        return tokenFromXsts(xstsToken, userHash);
    }
    public static String[] xstsAndHashFromMicrosoft(String code) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://xsts.auth.xboxlive.com/xsts/authorize");
        request.setHeader("Content-Type", "application/json");
        String requestBody = String.format("{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"%s\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}", code);
        request.setEntity(new StringEntity(requestBody));
        CloseableHttpResponse response = client.execute(request);
        String jsonString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        String userHash = jsonObject.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();
        String xstsToken = jsonObject.get("Token").getAsString();
        return new String[]{xstsToken, userHash};
    }
    public static String tokenFromXsts(String code, String userHash) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://api.minecraftservices.com/authentication/login_with_xbox");
        request.setHeader("Content-Type", "application/json");
        String requestBody = String.format("{ \"identityToken\": \"XBL3.0 x=%s;%s\"}", userHash, code);
        request.setEntity(new StringEntity(requestBody));
        CloseableHttpResponse response = client.execute(request);
        String jsonString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        String mcToken = jsonObject.get("access_token").getAsString();
        return mcToken;
    }
    public static Boolean validateSession(String token) throws IOException {
        try {
            String[] profileInfo = getProfileInfo(token);
            String IGN = profileInfo[0];
            String UUID = profileInfo[1];
            return IGN != null && UUID != null;
        } catch (Exception e) {
            return false;
        }
    }
    public static int changeName(String newName,String token) throws IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut request = new HttpPut("https://api.minecraftservices.com/minecraft/profile/name/" + newName);
        request.setHeader("Authorization", "Bearer " + token);
        CloseableHttpResponse response = client.execute(request);
        return response.getStatusLine().getStatusCode();
    }
    public static int changeSkin(String url,String token) throws IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://api.minecraftservices.com/minecraft/profile/skins");
        request.setHeader("Authorization", "Bearer " + token);
        request.setHeader("Content-Type", "application/json");
        String jsonString = String.format("{ \"variant\": \"classic\", \"url\": \"%s\"}", url);
        request.setEntity(new StringEntity(jsonString));
        CloseableHttpResponse response = client.execute(request);
        return response.getStatusLine().getStatusCode();
    }

}