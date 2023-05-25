package backend.security;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public static String encryptMD5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(string.getBytes(StandardCharsets.UTF_8));
            BigInteger asNumber = new BigInteger(1, digest);
            String hashedString = asNumber.toString(16);
            while (hashedString.length() < 32) {
                hashedString = "0" + hashedString;
            }
            return hashedString;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Encoding exception: MD5 algorithm not found!");
            return "";
        }
    }
}
