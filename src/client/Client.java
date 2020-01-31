package client;

import java.util.ArrayList;
import java.util.List;
import model.Coordinate;
import model.Direction;
import model.FullRoute;
import model.Mode;
import model.Privilege;
import model.Route;
import model.TransportMode;
import model.Type;
import model.User;

/**
 * The Interface for the HTTP Client.
 *
 * @author Jon Calvo Gaminde
 */
public interface Client {

    public void setCode(String code);

    //Route Client
    /**
     * A method that inserts a route in the server database.
     *
     * @param fullRoute The route to insert (includes the directions).
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public void createRoute(FullRoute fullRoute) throws Exception;

    /**
     * A method that edits an existent route in the server database.
     *
     * @param route The new route data.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public void editRoute(Route route) throws Exception;

    /**
     * A method that finds a specific route in the database by the unique id.
     *
     * @param routeId The id of the wanted route.
     * @return The wanted route, or null if it was not found.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public Route findRoute(String routeId) throws Exception;

    /**
     * A method that gets all routes from the database.
     *
     * @return A list containing all the routes.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public List<Route> findAllRoutes() throws Exception;

    /**
     * A method that gets all routes assigned to an specific user from the
     * database.
     *
     * @param userId The user id whose assigned routes we want to obtain.
     * @return A list containing all routes assigned to the user
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public List<Route> findRoutesByAssignedTo(String userId) throws Exception;

    /**
     * A method that deletes an existent route in the server database.
     *
     * @param routeId The id of the unwanted route.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public void removeRoute(String routeId) throws Exception;

    //Coordinate Client
    /**
     * A method that finds a specific coordinate in the database by the unique
     * id.
     *
     * @param coordinateId The id of the wanted coordinate.
     * @return The wanted coordinate, or null if it was not found.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public Coordinate findCoordinate(String coordinateId) throws Exception;

    /**
     * A method that gets all direction of certain type from the database.
     *
     * @param type The type of the directions wanted.
     * @return A list containing all the directions of that type.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public List<Direction> findDirectionsByType(Type type) throws Exception;

    /**
     * A method that gets all direction present in a specific route from the
     * database.
     *
     * @param routeId The id of the route whose directions we want.
     * @return A list containing all the directions present in the route.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public List<Direction> findDirectionsByRoute(String routeId) throws Exception;

    //User Client
    /**
     * A method that inserts a user in the server database.
     *
     * @param user The user to insert.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public void createUser(User user) throws Exception;

    /**
     * A method that edits an existent route in the server database.
     *
     * @param user The new user data.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public void editUser(User user) throws Exception;

    /**
     * A method that finds a specific user in the database by the unique id.
     *
     * @param userId The id of the wanted user.
     * @return The wanted user, or null if it was not found.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public User findUser(String userId) throws Exception;

    /**
     * A method that gets all users from the database.
     *
     * @return A list containing all the users.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public List<User> findAllUsers() throws Exception;

    /**
     * A method that finds user in the database by the unique login.
     *
     * @param userLogin The login of the user.
     * @return The user with that login, or null if it does not exists.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public User findUserByLogin(String userLogin) throws Exception;

    /**
     * A method that gets all users of certain privilege level from the
     * database.
     *
     * @param privilege The privilege level of the users wanted.
     * @return A list containing all the users of that privilege level.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public List<User> findUsersByPrivilege(Privilege privilege) throws Exception;

    /**
     * A method that deletes an existent user in the server database.
     *
     * @param userId The id of the unwanted user.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public void removeUser(String userId) throws Exception;

    /**
     * A method that checks the login and the password of a user and returns its
     * session, setting its code on the client.
     *
     * @param loginData An user with the login and the password.
     * @return A Session with the session code and the data of the user.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public User login(User loginData) throws Exception;

    /**
     * A method that asks the server to send a new password for the user to his
     * email.
     *
     * @param userData An user with the login and data of the user.
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public void forgottenPassword(User userData) throws Exception;

    /**
     * A method that asks the server to send a confirmation code for the user to
     * his email, and a hashed version to the client to compare.
     *
     * @param user The user that will receive de email.
     * @return The hashed code to compare
     * @throws Exception An Exception with a message ready to be show to the
     * user.
     */
    public String emailConfirmation(User user) throws Exception;

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
    public Direction getDirection(String direction, Type type) throws LogicBusinessException;

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
    public Route getRoute(ArrayList<String> coords, Route route) throws LogicBusinessException;

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
    public int[][] getMatrix(ArrayList<String> coords, Mode mode, TransportMode transport) throws LogicBusinessException;
}
