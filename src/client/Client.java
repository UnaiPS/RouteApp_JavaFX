/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import java.util.List;
import model.Coordinate;
import model.Coordinate_Route;
import model.Direction;
import model.FullRoute;
import model.Privilege;
import model.Route;
import model.Type;
import model.User;

/**
 *
 * @author Jon Calvo Gaminde
 */
public interface Client {

    //Route Client

    public void createRoute(FullRoute fullRoute);
    
    public void editRoute(FullRoute fullRoute);
    
    public Route findRoute(String routeId);
    
    public List<Route> findAllRoutes();
    
    public List<Route> findRoutesByAssignedTo(String userId);
    
    public void removeRoute(String routeId);
    
    //Coordinate Client
    
    public List<Direction> findDirectionsByType(Type type);
    
    public List<Direction> findDirectionsByRoute(String routeId);
    
    public void markDestinationAsVisited(Coordinate gpsCoordinate, Coordinate_Route visitedDestination);
    
    //User Client
    
    public void createUser(User user);
    
    public void editUser(User user);
    
    public User findUser(String userId);
    
    public List<User> findAllUsers();
    
    public User findUserByLogin(String userLogin);
    
    public List<User> findUsersByPrivilege(Privilege privilege);
    
    public void removeUser(String userId);
    
    public User login(User loginData);
    
    public void forgottenPassword(User userData);
    
    public String emailConfirmation(User user);
    
    public Direction getDirection(String direction) throws LogicBusinessException;
    
    public Route getRoute(ArrayList<String> coords, Route route) throws LogicBusinessException;
    
    public int restorePassword(String email, String login) throws LogicBusinessException;
}
