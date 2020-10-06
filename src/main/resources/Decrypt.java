package %PACKAGE%;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Placeholders:
 * % PACKAGE % : %PACKAGE%
 * % KEY_PROVIDER % : %KEY_PROVIDER_CLASS%
 */
public class Decrypt {
    private Decrypt() {
    }

    private static SecretKeySpec secretKey;
    private static boolean isKeySet = false;
    private static boolean notifiedInvalidKey = false;

    private static final Map<String, String> decrypted = new HashMap<>();

    private static void setKey(String keyStr) {
        try {
            byte[] key = keyStr.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            notifyInvalidKey();
        }
    }

    public static String decrypt(String str) {
        if(!decrypted.containsKey(str)) {
            if(!isKeySet) {
                String key = %KEY_PROVIDER_CLASS%.provideKey();
                setKey(key);

                isKeySet = true;
            }

            try {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

                String decryptedStr = new String(cipher.doFinal(Base64.getDecoder().decode(str)));
                decryptedStr = decryptedStr.intern(); // Force the string in the string pool

                decrypted.put(str, decryptedStr);
            }catch (Exception e) {
                notifyInvalidKey();
            }
        }

        return decrypted.get(str);
    }

    private static void notifyInvalidKey() {
        if(!notifiedInvalidKey) {
            notifiedInvalidKey = true;
            %KEY_PROVIDER_CLASS%.onInvalidKey();
        }
    }
}
