/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx.view;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author 2dam
 */
public class DirectionTvBean {
    private SimpleStringProperty name;
    
    public DirectionTvBean(String name){
        this.name = new SimpleStringProperty(name);
    }
    
    public String getName(){
        return this.name.get();
    }
    
    public void setName(String name){
        this.name.set(name);
    }
}
