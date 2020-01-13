/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Route;

/**
 * FXML Controller class
 *
 * @author Unai Pérez Sánchez
 */
public class RouteInfoController {
    
    private Logger LOGGER = Logger.getLogger("retoLogin.view.FXMLDocumentControllerSignUp");
    
    private Stage stage;
    
    private Route route;
    
    @FXML
    private TextField routeName;
    @FXML
    private TextField assignedTo;
    @FXML
    private TextField totalDistance;
    @FXML
    private TextField estimatedTime;
    @FXML
    private CheckBox isStarted;
    @FXML
    private CheckBox isEnded;
    @FXML
    private ComboBox<?> mode;
    @FXML
    private ComboBox<?> transportMode;
    @FXML
    private ComboBox<?> trafficMode;
    @FXML
    private Button btnUpdateAssingRoute;
    @FXML
    private Button btnSaveChanges;
    @FXML
    private TableView<?> directionsInfo;
    @FXML
    private TableColumn<?, ?> tblType;
    @FXML
    private TableColumn<?, ?> tblContry;
    @FXML
    private TableColumn<?, ?> tblState;
    @FXML
    private TableColumn<?, ?> tblCounty;
    @FXML
    private TableColumn<?, ?> tblCity;
    @FXML
    private TableColumn<?, ?> tblDistrict;
    @FXML
    private TableColumn<?, ?> tblStreet;
    @FXML
    private TableColumn<?, ?> tblHouseNumber;
    @FXML
    private TableColumn<?, ?> tblPostalCode;
    @FXML
    private Button btnReturnToMainMenu;
    @FXML
    private TextField createdBy;

    /**
     * Initializes the controller class.
     */
    public void initStage(Parent root) {
        LOGGER.info("Initializing Route Info stage");
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        /*
        The window will not be resizable
        */
        stage.setResizable(false);
        stage.setTitle("Route Info");
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::handleWindowClosing);
        stage.show();
    }
    
    public void handleWindowShowing(WindowEvent e){
        
    }
    
    public void handleWindowClosing(WindowEvent e){
        LOGGER.info("The window was attempted to be closed");
    }
    
    public void setRoute(Route route){
        this.route=route;
    }
    /**
     * Setter for the stage
     * @param stage Object of type Stage
     */
    public void setStage(Stage stage){
        this.stage=stage;
    }
    /**
     * Getter for the stage
     * @return Object of type Stage
     */
    public Stage getStage(){
        return stage;
    }
}
