/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 2dam
 */
@XmlRootElement
public class Coordinate_Route implements Serializable {
   
    private Coordinate_RouteId id;
    private Route route;
    private Coordinate coordinate;
    private Integer wayOrder;
    private Long visited;

    /**
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * @return the coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @param coordinate the coordinate to set
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * @return the order
     */
    public Integer getOrder() {
        return wayOrder;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Integer order) {
        this.wayOrder = order;
    }

    /**
     * @return the visited
     */
    public Long getVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(Long visited) {
        this.visited = visited;
    }

    
}
