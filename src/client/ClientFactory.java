package client;

/**
 * The Factory to get the Client Implementation.
 *
 * @author Jon Calvo Gaminde
 */
public class ClientFactory {

    private static Client client;

    //Getters
    public static Client getClient() {
        if (client == null) {
            client = new ClientImplementation();
        }
        return client;
    }
}
