/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:CoordinateFacadeREST
 * [routeappjpa.coordinate]<br>
 * USAGE:
 * <pre>
 *        ClientCoordinate client = new ClientCoordinate();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Unai Pérez Sánchez
 */
public class ClientCoordinate {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/RouteApp_Server/webresources";

    public ClientCoordinate() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("routeappjpa.coordinate");
    }

    public <T> T findDirectionsByType(String code, GenericType<T> responseType, String type) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("direction/type/{0}/{1}", new Object[]{code,type}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    
    public <T> T findDirectionsByRoute(String code, GenericType<T> responseType, String route) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("direction/route/{0}/{1}", new Object[]{code,route}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void markDestinationVisited(String code, Object requestEntity, String latitude, String longitude) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("direction/visited/{0}/{1}/{2}", new Object[]{code,latitude,longitude})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    /*
    public void createDirection(Object requestEntity) throws ClientErrorException {
        webTarget.path("direction").request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void createCoordinate(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T findByType(Class<T> responseType, String type) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("type/{0}", new Object[]{type}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    */
    public void close() {
        client.close();
    }
    
}
