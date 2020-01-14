/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx.logic;

/**
 * The factory of the Client.
 * @author Daira Eguzkiza
 */
public class ClientFactory {
    /**
     * Creates a new Client Object and returns it.
     * @return The new Client object.
     */
    public static Client getClient(){
        return new ClientImplementation();
    }
    
}
