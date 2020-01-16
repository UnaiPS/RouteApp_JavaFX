/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx.logic;

import beans.Coordinate;
import beans.Direction;
import beans.Mode;
import beans.Route;
import beans.TrafficMode;
import beans.TransportMode;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import beans.User;
import java.net.ProtocolException;
import java.util.ResourceBundle;

/**
 * This is the class that implements the Client methods.
 * @author Daira Eguzkiza
 */
public class ClientImplementation implements Client {

    private static final Logger LOGGER = Logger
            .getLogger("retoLogin.control.ClientImplementation");

    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //TODO: ID KEY Y ESO EN PROPERTIES
    
    /**
     * This method connects with the external web service: Here REST API.
     * This searches a direction based on the petition of the user and returns it.
     * @param sitio string representing the direction the user wants
     * @return the full direction the web service has found with the given one.
     * @throws LogicBusinessException 
     */
    @Override
    public Direction getDirection(String sitio) throws LogicBusinessException{
        Direction direction = new Direction();
        try {
            String inline="";
            
            sitio = sitio.replace(" ", "%20");
            ResourceBundle properties = ResourceBundle.getBundle("routeapp_javafx.logic.appid");
                String appid = properties.getString("app_id");
            URL url = new URL("https://geocoder.api.here.com/6.2/geocode.json?searchtext=" + sitio + appid + "&language=en-en");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET"); 
            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200)
            throw new RuntimeException("HttpResponseCode: " +responsecode);
            else
            {
                Scanner sc = new Scanner(url.openStream());
                while(sc.hasNext())
                {
                    inline+=sc.nextLine();
                }
                System.out.println("\nJSON data in string format");
                System.out.println(inline);
                sc.close();
                
                JSONParser parse = new JSONParser();
                JSONObject response = (JSONObject)parse.parse(inline);
                JSONObject jsonarr_1 = (JSONObject) response.get("Response");
                //JSONObject jsonarr_2 = (JSONObject) jsonarr_1.get("MetaInfo");
                JSONArray arrayView = (JSONArray) jsonarr_1.get("View");
                JSONObject prim = (JSONObject) arrayView.get(0);
                JSONArray arrayResponse = (JSONArray) prim.get("Result");
                JSONObject sec = (JSONObject) arrayResponse.get(0);
                JSONObject objLocation = (JSONObject) sec.get("Location");
                JSONObject objAddress = (JSONObject) objLocation.get("Address");
                JSONArray arrayNavigation = (JSONArray) objLocation.get("NavigationPosition");
                JSONObject objNavigation = (JSONObject) arrayNavigation.get(0);

                
                Coordinate coordinates = new Coordinate();
                coordinates.setLatitude((Double) objNavigation.get("Latitude"));
                coordinates.setLongitude((Double) objNavigation.get("Longitude"));
                
                
                
                direction.setCoordinate(coordinates);
                direction.setCity((String) objAddress.get("Address"));
                direction.setCountry((String) objAddress.get("Country"));
                direction.setCounty((String) objAddress.get("County"));
                direction.setName((String) objAddress.get("Label"));
                String cp = (String) objAddress.get("PostalCode");
                int CP = Integer.parseInt(cp);
                direction.setPostalCode(CP);
                direction.setState((String) objAddress.get("State"));
                direction.setStreet((String) objAddress.get("Street"));
                direction.setCity((String) objAddress.get("City"));
                direction.setDistrict((String) objAddress.get("District"));
                
                direction.setHouseNumber((String) objAddress.get("HouseNumber"));
                String hi = "";
            }    
        } catch (MalformedURLException ex) {
              LOGGER.log(Level.SEVERE,
                    "External web service: Exception getting the direction, {0}",
                    ex.getMessage());
            throw new LogicBusinessException("Error getting delivery users list:\n"+ex
                    .getMessage());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,
                    "Error: Exception reading/writing, {0}",
                    ex.getMessage());
            throw new LogicBusinessException("Error writing o reading file:\n"+ex
                    .getMessage());
        } catch (org.json.simple.parser.ParseException ex) {
            LOGGER.log(Level.SEVERE,
                    "JSON Parser: Exception getting the direction, {0}",
                    ex.getMessage());
            throw new LogicBusinessException("Error parsing data:\n"+ex
                    .getMessage());
        }
        return direction;
    }

    @Override
    public List<User> getDeliveryUsers() throws LogicBusinessException {
        try{
            UserRESTClient prueba = new UserRESTClient();
        List<User> deliveryUsers;
        return prueba.findAllDeliveryAccounts(new GenericType<List<User>>(){});
        }catch(Exception e){
            LOGGER.log(Level.SEVERE,
                    "UsersManager: Exception getting delivery users, {0}",
                    e.getMessage());
            throw new LogicBusinessException("Error getting delivery users list:\n"+e
                    .getMessage());
        }
        
    }

    @Override
    public int restorePassword(String email, String login) throws LogicBusinessException {
        try{
             UserRESTClient prueba = new UserRESTClient();
        int result = prueba.forgottenpasswd(int.class, email, login);
        return result;
        }catch(Exception e){
            LOGGER.log(Level.SEVERE,
                    "UsersManager: Exception restoring password, {0}",
                    e.getMessage());
            throw new LogicBusinessException("Error restoring the password:\n"+e
                    .getMessage());
        }
       
    }

    @Override
    public Route getRoute(ArrayList<String> coords, Route route) throws LogicBusinessException {
        String mode = "";        
        String consulta = "";
        
        if(route.getMode() == Mode.FASTEST) mode = "fastest";
        else if(route.getMode() == Mode.BALANCED) mode = "balanced";
        else mode = "shortest";
        
        
        if(route.getTransportMode() == TransportMode.CAR) mode += ";car";
        else if(route.getTransportMode() == TransportMode.CAR_HOV) mode += ";car_hov";
        else if(route.getTransportMode() == TransportMode.PEDESTRIAN) mode += ";pedestrian";
        else if(route.getTransportMode() == TransportMode.TRUCK) mode += ";truck";
        
        
        if(route.getTrafficMode() == TrafficMode.DISABLED)
        mode += ";traffic:disabled"; 
        else mode += ";traffic:disabled";
        
        for(int i = 0; i<coords.size(); i++){
            consulta += "&waypoint" + i + "=" + coords.get(i);
        }
        consulta += "&mode=" + mode;
        
        try {
            ResourceBundle properties = ResourceBundle.getBundle("routeapp_javafx.logic.appid");
                String appid = properties.getString("app_id");
            String inline = "";
            URL url = new URL("https://route.api.here.com/routing/7.2/calculateroute.json?" + consulta + appid);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET"); 
            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200)
                throw new RuntimeException("HttpResponseCode: " +responsecode);
            else
            {
                Scanner sc = new Scanner(url.openStream());
                while(sc.hasNext())
                {
                    inline+=sc.nextLine();
                }
                System.out.println("\nJSON data in string format");
                System.out.println(inline);
                sc.close();
                
                JSONParser parse = new JSONParser();
                JSONObject response = (JSONObject)parse.parse(inline);
                JSONObject jsonarr_1 = (JSONObject) response.get("response");
                JSONArray arrayView = (JSONArray) jsonarr_1.get("route");
                JSONObject prim = (JSONObject) arrayView.get(0);
                JSONObject objSummary = (JSONObject) prim.get("summary");
                String distance = (String)objSummary.get("distance").toString();
                String baseTime = (String)objSummary.get("baseTime").toString();
                String trafficTime = (String)objSummary.get("trafficTime").toString();
                
                int time = Integer.parseInt(baseTime) + Integer.parseInt(trafficTime);
                Double distance2 = Double.parseDouble(distance);
                route.setEstimatedTime(time);
                route.setTotalDistance(distance2);
            } 
        } catch (MalformedURLException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException | org.json.simple.parser.ParseException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    return route;
    
}
    
}
