package mushroom.Libs.IRC;

import com.google.gson.Gson;
import mushroom.GUI.Configs;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.Consumer;

public class ShroomIrcClient {

    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static boolean loaded = true;
    private final IrcClient client = new IrcClient(
            "irc.freenode.net",
            6667,
            RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.randomAlphanumeric(8),
            "#shroomclient",
            () -> System.out.println("IRC Connected"),
            () -> System.out.println("IRC Disconnected"),
            () -> {},
            (msg) -> onMessage(msg),
            (ignored) -> {},
            (ex) -> ex.printStackTrace()
    );

    public void start() {
        client.start();
    }

    public void stop() {
        client.stop();
    }

    private final String username;
    private final String clientBrand;
    private final Consumer<ShroomMessage> onMessage;
    public ShroomIrcClient(String username, String clientBrand, Consumer<ShroomMessage> onMessage) throws Exception {
        this.username = username;
        this.clientBrand = clientBrand;
        this.onMessage = onMessage;
    }

    private final String AES_KEY = "mooshroomclientisbetterthanmushroomclientisbetterthanadeptisbetterthanrise";
    private final String AES_SALT = "\"I'm losing, it's so over! I just got 20 hearted!\" - Scoliosis";
    private final SecretKey key = new SecretKeySpec(
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(
                    new PBEKeySpec(AES_KEY.toCharArray(), AES_SALT.getBytes(), 65536, 256)
            ).getEncoded(),
            "AES"
    );

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private String encrypt(String input) throws Exception {
        IvParameterSpec iv = generateIv();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(concat(iv.getIV(), cipherText));
    }

    private String decrypt(String input) throws Exception {
        byte[] payload = Base64.getDecoder().decode(input);
        byte[] iv = Arrays.copyOfRange(payload, 0, 16);
        byte[] encrypted = Arrays.copyOfRange(payload, 16, payload.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] plainText = cipher.doFinal(encrypted);
        return new String(plainText);
    }

    public class ShroomMessage {
        public String username;
        public String clientBrand;
        public String message;
    }

    public void sendMessage(String message) throws Exception {
        ShroomMessage m = new ShroomMessage();
        m.username = username;
        m.clientBrand = clientBrand;
        m.message = message;
        client.sendMessage(encrypt(new Gson().toJson(m)));
    }

    private void onMessage(IrcClient.IRCMessage msg) {
        try {
            String content = decrypt(msg.message);
            ShroomMessage m = new Gson().fromJson(content, ShroomMessage.class);
            onMessage.accept(m);
        } catch (Exception ignored) {}
    }
}