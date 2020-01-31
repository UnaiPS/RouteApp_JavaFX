package client;

import encryption.Encrypt;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.GenericType;
import model.Coordinate;
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

/**
 * The implemetation of the Client Interface.
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
    ResourceBundle properties = ResourceBundle.getBundle("clientconfig");
    private final String HERE_ID = properties.getString("hereApiId");
    private final String HERE_CODE = properties.getString("hereApiCode");
    private final String SERVER_IP = properties.getString("serverIp");
    private final String SERVER_PORT = properties.getString("serverPort");

    //Constructors
    /**
     * This constructor creates the connection URL using the data stored in the
     * properties file.
     */
    public ClientImplementation() {
        code = "";
        String baseURI = "http://" + SERVER_IP + ":" + SERVER_PORT + "/RouteApp_Server/webresources";
        clientRoute = new ClientRoute(baseURI);
        clientCoordinate = new ClientCoordinate(baseURI);
        clientUser = new ClientUser(baseURI);
    }

    //Setters
    @Override
    public void setCode(String code) {
        this.code = code;
    }

    //Getters
    /**
     * This getters gets a valit Session code with the client code.
     *
     * @return A newly created Session code.
     */
    public String getSessionCode() {
        String fullCode = code + Time.from(Instant.now()).getTime();
        return Encrypt.cifrarTexto(fullCode);
    }

    /**
     * This methods creates a dialog that the user must use to login again if he
     * has been inative for too long.
     */
    private void reLogin() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Re-login requiered");
        dialog.setHeaderText("Due to inactivity, you must login again.\nMake sure your system clock is in sync with the Internet time.");
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
            if (dialogButton == ButtonType.OK) {
                return tf.getText().length() + tf.getText() + pf.getText();
            } else {
                return null;
            }
        });

        dialog.getDialogPane().setContent(hBox);
        Optional<String> loginResult = dialog.showAndWait();

        // The user is given a choice between two options: he must login or close the application.
        if (loginResult.isPresent()) {
            User loginData = new User();
            int loginLegth = Character.getNumericValue(loginResult.get().charAt(0));
            loginData.setLogin(loginResult.get().substring(1, loginLegth + 1));
            loginData.setPassword(loginResult.get().substring(loginLegth + 1));
            try {
                login(loginData);
            } catch (Exception ex) {
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
    /**
     * A method that inserts a route in the server database.
     *
     * @param fullRoute The route to insert (includes the directions).
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public void createRoute(FullRoute fullRoute) throws Exception {
        try {
            clientRoute.create(getSessionCode(), fullRoute);
        } catch (NotAuthorizedException ex) {
            reLogin();
            createRoute(fullRoute);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
    }

    /**
     * A method that edits an existent route in the server database.
     *
     * @param route The new route data.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public void editRoute(Route route) throws Exception {
        try {
            clientRoute.edit(getSessionCode(), route);
        } catch (NotAuthorizedException ex) {
            reLogin();
            editRoute(route);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
    }

    /**
     * A method that finds a specific route in the database by the unique id.
     *
     * @param routeId The id of the wanted route.
     * @return The wanted route, or null if it was not found.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public Route findRoute(String routeId) throws Exception {
        Route result = null;
        try {
            result = clientRoute.find(getSessionCode(), Route.class, routeId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findRoute(routeId);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that gets all routes from the database.
     *
     * @return A list containing all the routes.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public List<Route> findAllRoutes() throws Exception {
        List<Route> result = null;
        try {
            result = clientRoute.findAll(getSessionCode(), new GenericType<List<Route>>() {
            });
        } catch (NotAuthorizedException ex) {
            reLogin();
            result = findAllRoutes();
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that gets all routes assigned to an specific user from the
     * database.
     *
     * @param userId The user id whose assigned routes we want to obtain.
     * @return A list containing all routes assigned to the user
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public List<Route> findRoutesByAssignedTo(String userId) throws Exception {
        List<Route> result = null;
        try {
            result = clientRoute.findByAssignedTo(getSessionCode(), new GenericType<List<Route>>() {
            }, userId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findRoutesByAssignedTo(userId);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that deletes an existent route in the server database.
     *
     * @param routeId The id of the unwanted route.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public void removeRoute(String routeId) throws Exception {
        try {
            clientRoute.remove(getSessionCode(), routeId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            removeRoute(routeId);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
    }

    //Coordinate ClientImplementation
    /**
     * A method that finds a specific coordinate in the database by the unique
     * id.
     *
     * @param coordinateId The id of the wanted coordinate.
     * @return The wanted coordinate, or null if it was not found.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public Coordinate findCoordinate(String coordinateId) throws Exception {
        Coordinate result = null;
        try {
            result = clientCoordinate.find(getSessionCode(), Coordinate.class, coordinateId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findCoordinate(coordinateId);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that gets all direction of certain type from the database.
     *
     * @param type The type of the directions wanted.
     * @return A list containing all the directions of that type.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public List<Direction> findDirectionsByType(Type type) throws Exception {
        List<Direction> result = null;
        try {
            result = clientCoordinate.findDirectionsByType(getSessionCode(), new GenericType<List<Direction>>() {
            }, type.name());
        } catch (NotAuthorizedException ex) {
            reLogin();
            findDirectionsByType(type);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that gets all direction present in a specific route from the
     * database.
     *
     * @param routeId The id of the route whose directions we want.
     * @return A list containing all the directions present in the route.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public List<Direction> findDirectionsByRoute(String routeId) throws Exception {
        List<Direction> result = null;
        try {
            result = clientCoordinate.findDirectionsByRoute(getSessionCode(), new GenericType<List<Direction>>() {
            }, routeId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findDirectionsByRoute(routeId);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    //User ClientImplementation
    /**
     * A method that inserts a user in the server database.
     *
     * @param user The user to insert.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public void createUser(User user) throws Exception {
        try {
            user.setPassword(Encrypt.cifrarTexto(user.getPassword()));
            clientUser.create(user);
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
    }

    /**
     * A method that edits an existent route in the server database.
     *
     * @param user The new user data.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public void editUser(User user) throws Exception {
        try {
            clientUser.edit(getSessionCode(), user);
        } catch (NotAuthorizedException ex) {
            reLogin();
            editUser(user);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
    }

    /**
     * A method that finds a specific user in the database by the unique id.
     *
     * @param userId The id of the wanted user.
     * @return The wanted user, or null if it was not found.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public User findUser(String userId) throws Exception {
        User result = null;
        try {
            result = clientUser.find(getSessionCode(), User.class, userId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findUser(userId);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that gets all users from the database.
     *
     * @return A list containing all the users.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public List<User> findAllUsers() throws Exception {
        List<User> result = null;
        try {
            result = clientUser.findAll(getSessionCode(), new GenericType<List<User>>() {
            });
        } catch (NotAuthorizedException ex) {
            reLogin();
            findAllUsers();
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that finds user in the database by the unique login.
     *
     * @param userLogin The login of the user.
     * @return The user with that login, or null if it does not exists.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public User findUserByLogin(String userLogin) throws Exception {
        User result = null;
        try {
            result = clientUser.findAccountByLogin(getSessionCode(), User.class, userLogin);
        } catch (NotAuthorizedException ex) {
            reLogin();
            findUserByLogin(userLogin);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that gets all users of certain privilege level from the
     * database.
     *
     * @param privilege The privilege level of the users wanted.
     * @return A list containing all the users of that privilege level.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public List<User> findUsersByPrivilege(Privilege privilege) throws Exception {
        List<User> result = null;
        try {
            result = clientUser.findByPrivilege(getSessionCode(), new GenericType<List<User>>() {
            }, privilege.name());
        } catch (NotAuthorizedException ex) {
            reLogin();
            findUsersByPrivilege(privilege);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    /**
     * A method that deletes an existent user in the server database.
     *
     * @param userId The id of the unwanted user.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public void removeUser(String userId) throws Exception {
        try {
            clientUser.remove(getSessionCode(), userId);
        } catch (NotAuthorizedException ex) {
            reLogin();
            removeUser(userId);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
    }

    /**
     * A method that checks the login and the password of a user and returns its
     * session, setting its code on the client.
     *
     * @param loginData An user with the login and the password.
     * @return A Session with the session code and the data of the user.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public User login(User loginData) throws Exception {
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
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result.getLogged();
    }

    /**
     * A method that asks the server to send a new password for the user to his
     * email.
     *
     * @param userData An user with the login and data of the user.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public void forgottenPassword(User userData) throws Exception {
        try {
            clientUser.forgottenpasswd(userData.getEmail(), userData.getLogin());
        } catch (NotFoundException ex) {
            throw new Exception("No user with that login found.");
        } catch (NotAuthorizedException ex) {
            throw new Exception("The data was wrong.");
        } catch (ForbiddenException ex) {
            throw new Exception("The password was changed or restored recently, you must wait some time to restore password again.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
    }

    /**
     * A method that asks the server to send a confirmation code for the user to
     * his email, and a hashed version to the client to compare.
     *
     * @param user The user that will receive de email.
     * @return The hashed code to compare
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    @Override
    public String emailConfirmation(User user) throws Exception {
        String result = null;
        try {
            result = clientUser.emailConfirmation(getSessionCode(), user, String.class);
        } catch (NotAuthorizedException ex) {
            reLogin();
            emailConfirmation(user);
        } catch (ForbiddenException ex) {
            throw new Exception("Wrong privilege.");
        } catch (InternalServerErrorException ex) {
            throw new Exception("Unexpected error happened.");
        } catch (ServiceUnavailableException ex) {
            throw new Exception("Unable to process. Try again later.");
        }
        return result;
    }

    //Remote API
    /**
     * A method that asks the external API data about a place and creates a
     * direction of that point.
     *
     * @param place The string containing the name of the place.
     * @param type The type of direction.
     * @return The direction created with the data.
     * @throws LogicBusinessException An Exception with a message ready to be
     * show to the user.
     */
    @Override
    public Direction getDirection(String place, Type type) throws LogicBusinessException {
        Direction direction = new Direction();
        try {
            String inline = "";

            place = place.replace(" ", "%20");
            place = place.replace("ñ", "n");
            URL url = new URL("https://geocoder.api.here.com/6.2/geocode.json?searchtext=" + place + "&app_id=" + HERE_ID + "&app_code=" + HERE_CODE + "&language=en-en");
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
                } catch (NumberFormatException ex) {
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
        } catch (Exception ex) {
            LOGGER.severe("External web service: Exception getting the direction " + ex.getMessage());
            throw new LogicBusinessException("Error getting the direction.");
        }
        return direction;
    }

    /**
     * A method that asks the external API data about a route and creates a
     * route object with it.
     *
     * @param coords An array of Strings containing the directions for the
     * route.
     * @param route A route object with data about route mode.
     * @return The route object with the route coordinates.
     * @throws LogicBusinessException An Exception with a message ready to be
     * show to the user.
     */
    @Override
    public Route getRoute(ArrayList<String> coords, Route route) throws LogicBusinessException {
        String mode = "";
        String query = "";

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
            query += "&waypoint" + i + "=" + coords.get(i);
        }
        query += "&mode=" + mode;

        try {
            String inline = "";
            URL url = new URL("https://route.api.here.com/routing/7.2/calculateroute.json?app_id=" + HERE_ID + "&app_code=" + HERE_CODE + query);
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
        } catch (Exception ex) {
            LOGGER.severe("External web service: Exception getting the route " + ex.getMessage());
            throw new LogicBusinessException("Error getting the route.");
        }
        return route;
    }

    /**
     * A method that gets from the external API the cost matrix of coordinates,
     * and returns it.
     *
     * @param coords The coordinates to make the matrix.
     * @param mode The mode to calculate the cost values.
     * @param transport The transport for which calculate the cost values.
     * @return The cost matrix of the coordinates.
     * @throws LogicBusinessException An Exception with a message ready to be
     * show to the user.
     */
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
            URL url = new URL("https://matrix.route.api.here.com/routing/7.2/calculatematrix.json?" + query + "app_id=" + HERE_ID + "&app_code=" + HERE_CODE);
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
        } catch (Exception ex) {
            LOGGER.severe("External web service: Exception getting the matrix " + ex.getMessage());
            throw new LogicBusinessException("Error getting the route.");
        }
        return matrix;
    }
}
