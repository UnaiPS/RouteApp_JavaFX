/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx.view;

import java.util.ArrayList;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author 2dam
 */
public class DirectionTvManagerData implements DirectionTvManager{
private ObservableList<DirectionTvBean> directions = FXCollections.observableArrayList();    
    @Override
    public Collection getAllDirections() {
    return directions;  
    }
    
} 
