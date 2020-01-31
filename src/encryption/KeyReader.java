package encryption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The class that manages the read of the keys files.
 *
 * @author Unai Pérez Sánchez
 */
public class KeyReader {

    /**
     * A method that reads the key file and returns it.
     *
     * @param path The path of the key file.
     * @param isUrl If true, the file reader will work like it would in a Web
     * Application.
     * @return The key bytes.
     * @throws IOException A Exception throw during reading.
     */
    public byte[] fileReader(String path, Boolean isUrl) throws IOException {
        if (isUrl) {
            path = this.getClass().getResource(path).getFile();
        }
        File file = new File(path);
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ous.toByteArray();
    }
}
