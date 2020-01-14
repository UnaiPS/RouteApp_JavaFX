/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx.logic;

import beans.User;
import beans.Direction;
import beans.Route;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the interface of the Client methods.
 * @author Daira Eguzkiza
 */
public interface Client {
    public Direction getDirection(String direction) throws LogicBusinessException;
    public List<User> getDeliveryUsers() throws LogicBusinessException;
    public int restorePassword(String email, String login) throws LogicBusinessException;
    public Route getRoute(ArrayList<String> coords, Route route) throws LogicBusinessException;
    public String getCoords(String direction) throws LogicBusinessException;
    
}
