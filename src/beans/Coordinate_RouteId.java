/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author 2dam
 */
@Embeddable
public class Coordinate_RouteId implements Serializable {
    private Long routeId;
    private Long coordinateId;

    /**
     * @return the routeId
     */
    public Long getRouteId() {
        return routeId;
    }

    /**
     * @param routeId the routeId to set
     */
    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    /**
     * @return the coordinateId
     */
    public Long getCoordinateId() {
        return coordinateId;
    }

    /**
     * @param coordinateId the coordinateId to set
     */
    public void setCoordinateId(Long coordinateId) {
        this.coordinateId = coordinateId;
    }

    
}
