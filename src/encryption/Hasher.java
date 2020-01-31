package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 * The class that manages the hashing.
 *
 * @author Jon Calvo Gaminde
 */
public class Hasher {

    /**
     * A method that transform a string into an hashed hexadecimal string..
     *
     * @param data A string to be hashed.
     * @return The hashed hexadecimal string.
     */
    public static String encrypt(String data) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte dataBytes[] = data.getBytes();
            messageDigest.update(dataBytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Hasher.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
        }
        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }
}
