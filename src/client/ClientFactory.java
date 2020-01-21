/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Jon Calvo Gaminde
 */
public class ClientFactory {
    private static Client client;
    
    public static Client getClient() {
        if (client == null) {
            client = new ClientImplementation();
        }
        return client;
    }
}
