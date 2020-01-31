package encryption;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

/**
 * The class that manages the encrypt of the asymetric encryption.
 *
 * @author Unai Pérez Sánchez
 */
public class Encrypt {

    /**
     * A method that transform a string into an encrypted hexadecimal string..
     *
     * @param mensaje A string to be encrypted.
     * @return The encrypted hexadecimal string.
     */
    public static String cifrarTexto(String mensaje) {
        byte[] encodedMessage = null;
        KeyReader rsa = new KeyReader();
        try {

            // Public key
            byte fileKey[] = rsa.fileReader("tmp\\Public.key", false);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(fileKey);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedMessage = cipher.doFinal(mensaje.getBytes());
        } catch (Exception e) {
            Logger.getLogger(Encrypt.class.getName()).severe(e.getLocalizedMessage());
        }
        return DatatypeConverter.printHexBinary(encodedMessage);
    }

}
