package beans;

import javafx.beans.property.SimpleStringProperty;

/**
 * Table row bean that is used in FXMLDocumentCreateRouteController
 *
 * @author Daira Eguzkiza
 */
public class DirectionTvBean {

    private SimpleStringProperty name;

    //Constructors
    public DirectionTvBean(String name) {
        this.name = new SimpleStringProperty(name);
    }

    //Getters
    public String getName() {
        return this.name.get();
    }

    //Setters
    public void setName(String name) {
        this.name.set(name);
    }
}
