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
import model.Mode;
import model.Privilege;
import model.Route;
import model.TransportMode;
import model.Type;
import model.User;

/**
 *
 * @author Jon Calvo Gaminde
 */
public interface Client {
    
    public void setCode(String code );

    //Route Client

    public void createRoute(FullRoute fullRoute ) throws Exception;
    
    public void editRoute(Route route ) throws Exception;
    
    public Route findRoute(String routeId ) throws Exception;
    
    public List<Route> findAllRoutes( ) throws Exception;
    
    public List<Route> findRoutesByAssignedTo(String userId ) throws Exception;
    
    public void removeRoute(String routeId ) throws Exception;
    
    //Coordinate Client
    
    public Coordinate findCoordinate(String coordinateId ) throws Exception;
    
    public List<Direction> findDirectionsByType(Type type ) throws Exception;
    
    public List<Direction> findDirectionsByRoute(String routeId ) throws Exception;
    
    public void markDestinationAsVisited(Coordinate gpsCoordinate, Coordinate_Route visitedDestination ) throws Exception;
    
    //User Client
    
    public void createUser(User user ) throws Exception;
    
    public void editUser(User user ) throws Exception;
    
    public User findUser(String userId ) throws Exception;
    
    public List<User> findAllUsers( ) throws Exception;
    
    public User findUserByLogin(String userLogin ) throws Exception;
    
    public List<User> findUsersByPrivilege(Privilege privilege ) throws Exception;
    
    public void removeUser(String userId ) throws Exception;
    
    public User login(User loginData) throws Exception;
    
    public void forgottenPassword(User userData ) throws Exception;
    
    public String emailConfirmation(User user ) throws Exception;
    
    //Remote API
    
    public Direction getDirection(String direction, Type type) throws LogicBusinessException;
    
    public Route getRoute(ArrayList<String> coords, Route route) throws LogicBusinessException;
    
    public int[][] getMatrix(ArrayList<String> coords, Mode mode, TransportMode transport) throws LogicBusinessException;
    
    //public int restorePassword(String email, String login) throws LogicBusinessException;
}
