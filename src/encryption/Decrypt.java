package encryption;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

/**
 * The class that manages the decrypt of the asymetric encryption.
 *
 * @author Unai Pérez Sánchez
 */
public class Decrypt {
    /**
     * A method that transform an encrypted hexadecimal string into a decrypted
     * string.
     *
     * @param codedMessage An encrypted hexadecimal string to be decrypted.
     * @return The decrypted string.
     */
    public static String descifrarTexto(String codedMessage) {
        byte[] message = DatatypeConverter.parseHexBinary(codedMessage);
        KeyReader rsa = new KeyReader();

        byte[] decodedMessage = null;
        try {
            // Private key
            byte fileKey[] = rsa.fileReader("..\\keys\\Private.key", true);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(fileKey);
            PrivateKey privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decodedMessage = cipher.doFinal(message);
        } catch (Exception e) {
            Logger.getLogger(Decrypt.class.getName()).severe(e.getLocalizedMessage());
        }
        return new String(decodedMessage);
    }
}
