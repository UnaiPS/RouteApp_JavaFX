/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import encryption.Encrypt;
import encryption.Hasher;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.GenericType;
import model.Coordinate;
import model.Coordinate_Route;
import model.Direction;
import model.FullRoute;
import model.Privilege;
import model.Route;
import model.Session;
import model.Type;
import model.User;
import model.TrafficMode;
import model.Mode;
import model.TransportMode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import view.FXMLDocumentControllerUserProfile;

/**
 *
 * @author Jon Calvo Gaminde
 */
public class ClientImplementation implements Client {

    private static final Logger LOGGER = Logger
            .getLogger("client.ClientImplementation");
    private String code;
    private ClientRoute clientRoute;
    private ClientCoordinate clientCoordinate;
    private ClientUser clientUser;

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public ClientImplementation() {
        code = "";
        clientRoute = new ClientRoute();
        clientCoordinate = new ClientCoordinate();
        clientUser = new ClientUser();
    }

    public String getSessionCode() {
        String fullCode = code + Time.from(Instant.now()).getTime();
        return Encrypt.cifrarTexto(fullCode);
    }
    
    private void reLogin() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Re-login requiered");
        dialog.setHeaderText("Due to inactivity, you must login again.");
        dialog.setGraphic(null);

        TextField tf = new TextField();
        tf.setPromptText("Login");
        PasswordField pf = new PasswordField();
        pf.setPromptText("Password");


        HBox hBox = new HBox();
        hBox.getChildren().add(tf);
        hBox.getChildren().add(pf);
        hBox.setPadding(new Insets(20));

        dialog.setResultConverter(dialogButton -> {
        if (dialogButton == ButtonType.OK ) {
            return tf.getText().length() + tf.getText() + pf.getText();
        } else
            return null;
        });

        dialog.getDialogPane().setContent(hBox);
        Optional<String> loginResult = dialog.showAndWait();
        if (loginResult.isPresent()){
            User loginData = new User();
            int loginLegth = Character.getNumericValue(loginResult.get().charAt(0));
            loginData.setLogin(loginResult.get().substring(1, loginLegth+1));
            loginData.setPassword(loginResult.get().substring(loginLegth+1));
            try {
                login(loginData);
            } catch(Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(ex.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
            alert.setTitle("Close");
            alert.setHeaderText("This will close the aplication. Continue?");
            Optional<ButtonType> okButton = alert.showAndWait();
            if (okButton.isPresent() && okButton.get() == ButtonType.OK) {
                System.exit(0);
            }
        }
        
        
    }

    //Route ClientImplementation
    @Override
    public void createRoute(FullRoute fullRoute) {
        try {
            clientRoute.create(getSessionCode(), fullRoute);
        } catch (NotAuthorizedException ex) {
            reLogin();
            createRoute(fullRoute);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    @Override
    public void editRoute(Route route) {
        try {
            clientRoute.edit(getSessionCode(), route);
        } catch (NotAuthorizedException ex) {
            reLogin();
            editRoute(route);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    @Override
    public Route findRoute(String routeId) {
        Route result = null;
        try {
            result = clientRoute.find(getSessionCode(), Route.class, routeId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findRoute(routeId);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public List<Route> findAllRoutes() {
        List<Route> result = null;
        try {
            result = clientRoute.findAll(getSessionCode(), new GenericType<List<Route>>() {});
        } catch (NotAuthorizedException ex) {
            reLogin();
            result = findAllRoutes();
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public List<Route> findRoutesByAssignedTo(String userId) {
        List<Route> result = null;
        try {
            result = clientRoute.findByAssignedTo(getSessionCode(), new GenericType<List<Route>>() {}, userId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findRoutesByAssignedTo(userId);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public void removeRoute(String routeId) {
        try {
            clientRoute.remove(getSessionCode(), routeId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            removeRoute(routeId);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    //Coordinate ClientImplementation
    
    @Override
    public Coordinate findCoordinate(String coordinateId) {
        Coordinate result = null;
        try {
            result = clientCoordinate.find(getSessionCode(), Coordinate.class, coordinateId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findCoordinate(coordinateId);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }
    
    @Override
    public List<Direction> findDirectionsByType(Type type) {
        List<Direction> result = null;
        try {
            result = clientCoordinate.findDirectionsByType(getSessionCode(), new GenericType<List<Direction>>() {
            }, type.name());
        } catch (NotAuthorizedException ex) {
            reLogin();
            findDirectionsByType(type);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public List<Direction> findDirectionsByRoute(String routeId) {
        List<Direction> result = null;
        try {
            result = clientCoordinate.findDirectionsByRoute(getSessionCode(), new GenericType<List<Direction>>() {
            }, routeId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findDirectionsByRoute(routeId);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public void markDestinationAsVisited(Coordinate gpsCoordinate, Coordinate_Route visitedDestination) {
        try {
            clientCoordinate.markDestinationVisited(getSessionCode(), visitedDestination, String.valueOf(gpsCoordinate.getLatitude()), String.valueOf(gpsCoordinate.getLongitude()));
        } catch (NotAuthorizedException ex) {
            reLogin();
            markDestinationAsVisited(gpsCoordinate, visitedDestination);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    //User ClientImplementation
    @Override
    public void createUser(User user) {
        try {
            user.setPassword(Encrypt.cifrarTexto(user.getPassword()));
            clientUser.create(user);
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    @Override
    public void editUser(User user) {
        try {
            clientUser.edit(getSessionCode(), user);
        } catch (NotAuthorizedException ex) {
            reLogin();
            editUser(user);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    @Override
    public User findUser(String userId) {
        User result = null;
        try {
            result = clientUser.find(getSessionCode(), User.class, userId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findUser(userId);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> result = null;
        try {
            result = clientUser.findAll(getSessionCode(), new GenericType<List<User>>() {
            });
        } catch (NotAuthorizedException ex) {
            reLogin();
            findAllUsers();
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public User findUserByLogin(String userLogin) {
        User result = null;
        try {
            result = clientUser.findAccountByLogin(getSessionCode(), User.class, userLogin);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findUserByLogin(userLogin);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public List<User> findUsersByPrivilege(Privilege privilege) {
        List<User> result = null;
        try {
            result = clientUser.findByPrivilege(getSessionCode(), new GenericType<List<User>>() {
            }, privilege.name());
        } catch (NotAuthorizedException ex) {
            reLogin();
            findUsersByPrivilege(privilege);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }

    @Override
    public void removeUser(String userId) {
        try {
            clientUser.remove(getSessionCode(), userId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            removeUser(userId);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    @Override
    public User login(User loginData) throws Exception{
        Session result = null;
        try {
            loginData.setPassword(Encrypt.cifrarTexto(loginData.getPassword()));
            result = clientUser.login(loginData, Session.class);
            setCode(result.getCode());
        } catch (NotFoundException ex) {
            throw new Exception("No user exists for login: " + loginData.getLogin());
        } catch (NotAuthorizedException ex) {
            throw new Exception("The entered password is wrong.");
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result.getLogged();
    }

    @Override
    public void forgottenPassword(User userData) {
        try {
            clientUser.forgottenpasswd(userData.getEmail(), userData.getLogin());
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    @Override
    public String emailConfirmation(User user) {
        String result = null;
        try {
            result = clientUser.emailConfirmation(getSessionCode(), user, String.class);
        } catch (NotAuthorizedException ex) {
            reLogin();
            emailConfirmation(user);
        } catch (ForbiddenException ex) {
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }
    
    //Remote API

    @Override
    public Direction getDirection(String sitio, Type type) throws LogicBusinessException {
        Direction direction = new Direction();
        try {
            String inline = "";

            sitio = sitio.replace(" ", "%20");
            sitio = sitio.replace("ñ", "n");
            URL url = new URL("https://geocoder.api.here.com/6.2/geocode.json?searchtext=" + sitio + "&app_id=w4M9GIVbS5uVCLiCyGKV&app_code=JOPGDZHGQJ7FpUVmbfm4KA" + "&language=en-en");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inline += sc.nextLine();
                }
                System.out.println("\nJSON data in string format");
                System.out.println(inline);
                sc.close();

                JSONParser parse = new JSONParser();
                JSONObject response = (JSONObject) parse.parse(inline);
                JSONObject jsonarr_1 = (JSONObject) response.get("Response");
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
                coordinates.setType(type);

                direction.setCoordinate(coordinates);
                direction.setCity((String) objAddress.get("Address"));
                direction.setCountry((String) objAddress.get("Country"));
                direction.setCounty((String) objAddress.get("County"));
                direction.setName((String) objAddress.get("Label"));
                String cp = (String) objAddress.get("PostalCode");
                int postalCode = 0;
                try {
                    postalCode = Integer.parseInt(cp);
                } catch (NumberFormatException ex){
                    postalCode = 0;
                    LOGGER.warning("Foreign postal code.");
                }
                direction.setPostalCode(postalCode);
                direction.setState((String) objAddress.get("State"));
                direction.setStreet((String) objAddress.get("Street"));
                direction.setCity((String) objAddress.get("City"));
                direction.setDistrict((String) objAddress.get("District"));

                direction.setHouseNumber((String) objAddress.get("HouseNumber"));
            }
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.SEVERE,
                    "External web service: Exception getting the direction, {0}",
                    ex.getMessage());
            throw new LogicBusinessException("Error getting delivery users list:\n" + ex
                    .getMessage());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,
                    "Error: Exception reading/writing, {0}",
                    ex.getMessage());
            throw new LogicBusinessException("Error writing o reading file:\n" + ex
                    .getMessage());
        } catch (org.json.simple.parser.ParseException ex) {
            LOGGER.log(Level.SEVERE,
                    "JSON Parser: Exception getting the direction, {0}",
                    ex.getMessage());
            throw new LogicBusinessException("Error parsing data:\n" + ex
                    .getMessage());
        }
        return direction;
    }

    @Override
    public Route getRoute(ArrayList<String> coords, Route route) throws LogicBusinessException {
        String mode = "";
        String consulta = "";

        if (route.getMode() == Mode.FASTEST) {
            mode = "fastest";
        } else if (route.getMode() == Mode.BALANCED) {
            mode = "balanced";
        } else {
            mode = "shortest";
        }

        if (route.getTransportMode() == TransportMode.CAR) {
            mode += ";car";
        } else if (route.getTransportMode() == TransportMode.CAR_HOV) {
            mode += ";carHOV";
        } else if (route.getTransportMode() == TransportMode.PEDESTRIAN) {
            mode += ";pedestrian";
        } else if (route.getTransportMode() == TransportMode.TRUCK) {
            mode += ";truck";
        }

        if (route.getTransportMode() != TransportMode.PEDESTRIAN && route.getTrafficMode() == TrafficMode.DISABLED) {
            mode += ";traffic:disabled";
        } else {
            mode += ";traffic:enabled";
        }

        for (int i = 0; i < coords.size(); i++) {
            consulta += "&waypoint" + i + "=" + coords.get(i);
        }
        consulta += "&mode=" + mode;

        try {
            String inline = "";
            URL url = new URL("https://route.api.here.com/routing/7.2/calculateroute.json?app_id=w4M9GIVbS5uVCLiCyGKV&app_code=JOPGDZHGQJ7FpUVmbfm4KA" + consulta);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inline += sc.nextLine();
                }
                System.out.println("\nJSON data in string format");
                System.out.println(inline);
                sc.close();

                JSONParser parse = new JSONParser();
                JSONObject response = (JSONObject) parse.parse(inline);
                JSONObject jsonarr_1 = (JSONObject) response.get("response");
                JSONArray arrayView = (JSONArray) jsonarr_1.get("route");
                JSONObject prim = (JSONObject) arrayView.get(0);
                JSONObject objSummary = (JSONObject) prim.get("summary");
                String distance = (String) objSummary.get("distance").toString();
                String trafficTime = "0";
                if (route.getTransportMode() != TransportMode.PEDESTRIAN) {
                    trafficTime = (String) objSummary.get("trafficTime").toString();
                } 
                String baseTime = (String) objSummary.get("baseTime").toString();

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
    
    @Override
    public int[][] getMatrix(ArrayList<String> coords, Mode mode, TransportMode transport) throws LogicBusinessException {
        String extra;
        String query = "";
        int[][] matrix = new int[coords.size()][coords.size()];

        if (mode == Mode.FASTEST) {
            extra = "fastest";
        } else if (mode == Mode.BALANCED) {
            extra = "balanced";
        } else {
            extra = "shortest";
        }

        if (transport == TransportMode.CAR) {
            extra += ";car";
        } else if (transport == TransportMode.CAR_HOV) {
            extra += ";carHOV";
        } else if (transport == TransportMode.PEDESTRIAN) {
            extra += ";pedestrian";
        } else if (transport == TransportMode.TRUCK) {
            extra += ";truck";
        }

        for (int i = 0; i < coords.size(); i++) {
            query += "start" + i + "=" + coords.get(i) + "&";
        }
        for (int i = 0; i < coords.size(); i++) {
            query += "destination" + i + "=" + coords.get(i) + "&";
        }
        query += "mode=" + extra + "&";

        try {
            String inline = "";
            URL url = new URL("https://matrix.route.api.here.com/routing/7.2/calculatematrix.json?" + query + "app_id=w4M9GIVbS5uVCLiCyGKV&app_code=JOPGDZHGQJ7FpUVmbfm4KA");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inline += sc.nextLine();
                }
                System.out.println("\nJSON data in string format");
                System.out.println(inline);
                sc.close();

                JSONParser parse = new JSONParser();
                JSONObject response = (JSONObject) parse.parse(inline);
                JSONObject jsonarr_1 = (JSONObject) response.get("response");
                JSONArray arrayView = (JSONArray) jsonarr_1.get("matrixEntry");
                for (int i = 0; i < arrayView.size(); i++) {
                    JSONObject entry = (JSONObject) arrayView.get(i);
                    String n = (String) entry.get("startIndex").toString();
                    String m = (String) entry.get("destinationIndex").toString();
                    String cost = (String) ((JSONObject) entry.get("summary")).get("costFactor").toString();
                    matrix[Integer.parseInt(n)][Integer.parseInt(m)] = Integer.parseInt(cost);
                }

            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException | org.json.simple.parser.ParseException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return matrix;
    }

//    @Override
//    public int restorePassword(String email, String login) throws LogicBusinessException {
//        try {
//            
//            int result = clientUser.forgottenpasswd(int.class, email, login);
//            return result;
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE,
//                    "UsersManager: Exception restoring password, {0}",
//                    e.getMessage());
//            throw new LogicBusinessException("Error restoring the password:\n" + e
//                    .getMessage());
//        }
//
//    }

    
}