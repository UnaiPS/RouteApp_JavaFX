/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import routeapp_javafx.logic.User;

/**
 * This is the class that implements the Client methods.
 * @author Daira Eguzkiza, Jon Calvo Gaminde
 */
public class ClientImplementation implements Client {

    private static final Logger LOGGER = Logger
            .getLogger("retoLogin.control.ClientImplementation");

    @Override
    public Direction getDirection(String sitio) {
        Direction direction = new Direction();
        try {
            String inline="";
            
            sitio = sitio.replace(" ", "%20");
            URL url = new URL("https://geocoder.api.here.com/6.2/geocode.json?searchtext=" + sitio + "&app_id=w4M9GIVbS5uVCLiCyGKV&app_code=JOPGDZHGQJ7FpUVmbfm4KA");
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
            
        } catch (IOException ex) {
            
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return direction;
    }

    @Override
    public List<User> getDeliveryUsers() {
        UserRESTClient prueba = new UserRESTClient();
        List<User> deliveryUsers;
        
        deliveryUsers = prueba.findAllDeliveryAccounts(new GenericType<List<User>>(){});
        return deliveryUsers;
    }
    
    
    
}
