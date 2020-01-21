/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import encryption.Encrypt;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
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

/**
 *
 * @author Jon Calvo Gaminde
 */
public class ClientImplementation implements Client{

    private String code;
    private ClientRoute clientRoute;
    private ClientCoordinate clientCoordinate;
    private ClientUser clientUser;

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

    //Route ClientImplementation
    
    @Override
    public void createRoute(FullRoute fullRoute) {
        try {
            clientRoute.create(getSessionCode(), fullRoute);
        } catch (NotAuthorizedException ex) {
            //TODO
        } catch (ForbiddenException ex) { 
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    @Override
    public void editRoute(FullRoute fullRoute) {
        try {
            clientRoute.edit(getSessionCode(), fullRoute);
        } catch (NotAuthorizedException ex) {
            //TODO
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
            //TODO
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
            //TODO
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
            //TODO
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
            //TODO
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
    public List<Direction> findDirectionsByType(Type type) {
        List<Direction> result = null;
        try {
            result = clientCoordinate.findDirectionsByType(getSessionCode(), new GenericType<List<Direction>>() {}, type.name());
        } catch (NotAuthorizedException ex) {
            //TODO
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
            result = clientCoordinate.findDirectionsByRoute(getSessionCode(), new GenericType<List<Direction>>() {}, routeId);
        } catch (NotAuthorizedException ex) {
            //TODO
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
            //TODO
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
            //TODO
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
            //TODO
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
            result = clientUser.findAll(getSessionCode(), new GenericType<List<User>>() {});
        } catch (NotAuthorizedException ex) {
            //TODO
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
            result = clientUser.find(getSessionCode(), User.class, userLogin);
        } catch (NotAuthorizedException ex) {
            //TODO
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
            result = clientUser.findByPrivilege(getSessionCode(), new GenericType<List<User>>() {}, privilege.name());
        } catch (NotAuthorizedException ex) {
            //TODO
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
            //TODO
        } catch (ForbiddenException ex) { 
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
    }

    @Override
    public User login(User loginData) {
        Session result = null;
        try {
            loginData.setPassword(Encrypt.cifrarTexto(loginData.getPassword()));
            result = clientUser.login(loginData, Session.class);
            setCode(result.getCode());
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
            clientUser.forgottenpasswd(userData.getLogin(), userData.getEmail());
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
            //TODO
        } catch (ForbiddenException ex) { 
            //TODO
        } catch (InternalServerErrorException ex) {
            //TODO
        } catch (ServiceUnavailableException ex) {
            //TODO
        }
        return result;
    }
    
}
