/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import beans.Coordinate_Route;
import client.Client;
import client.ClientFactory;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import beans.Mode;
import beans.Route;
import beans.TrafficMode;
import beans.TransportMode;
import beans.User;
import client.ClientRoute;
import client.UserRESTClient;

/**
 * FXML Controller class
 *
 * @author Unai Pérez Sánchez
 */
public class RouteInfoController {
    
    private Logger LOGGER = Logger.getLogger("retoLogin.view.FXMLDocumentControllerSignUp");
    
    private Stage stage;
    
    private Route route;
    
    private ObservableList<Route> routes;
    
    private UserRESTClient userClient = new UserRESTClient();
    
    private ClientRoute routeClient = new ClientRoute();
    
    private Alert alert;
    
    private User user;
    
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
    private ComboBox<Mode> mode;
    @FXML
    private ComboBox<TransportMode> transportMode;
    @FXML
    private ComboBox<TrafficMode> trafficMode;
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
        LOGGER.info("Coordinates size: "+route.getCoordinates().size());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        /*
        The window will not be resizable
        */
        stage.setResizable(false);
        stage.setTitle("Route Info");
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::handleWindowClosing);
        
        btnReturnToMainMenu.setOnAction(this::handlebtnReturnToMainMenu);
        btnUpdateAssingRoute.setOnAction(this::handlebtnUpdateAssignRoute);
        btnSaveChanges.setOnAction(this::handlebtnSaveChanges);
        
        ObservableList<Mode> modes = FXCollections.observableArrayList();
        modes.add(Mode.FASTEST);
        modes.add(Mode.BALANCED);
        modes.add(Mode.SHORTEST);
        
        ObservableList<TransportMode> transModes = FXCollections.observableArrayList();
        transModes.add(TransportMode.CAR);
        transModes.add(TransportMode.BICYCLE);
        transModes.add(TransportMode.CAR_HOV);
        transModes.add(TransportMode.PEDESTRIAN);
        transModes.add(TransportMode.TRUCK);
        
        ObservableList<TrafficMode> traffModes = FXCollections.observableArrayList();
        traffModes.add(TrafficMode.ENABLED);
        traffModes.add(TrafficMode.DISABLED);
        
        mode.setItems(modes);
        transportMode.setItems(transModes);
        trafficMode.setItems(traffModes);
        
        stage.show();
    }
    
    public void handlebtnSaveChanges(ActionEvent e){
        route.setCoordinates(null);
        route.setAssignedTo(route.getAssignedTo());
        route.setMode(mode.getValue());
        route.setName(routeName.getText());
        route.setTrafficMode(trafficMode.getValue());
        route.setTransportMode(transportMode.getValue());
        try{
            routeClient.edit(route);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Main_Menu.fxml"));
            Parent root = null;
            try {
                root = (Parent) loader.load();
            } catch (IOException ex) {
                LOGGER.severe("Error: "+ex.getLocalizedMessage());
            }
            Admin_Main_MenuController viewController = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            viewController.setUser(user);
            viewController.setStage(stage);
            viewController.initStage(root);
            this.stage.close();
        }catch(Exception ex){
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
            alert = new Alert(Alert.AlertType.ERROR, "Unexpected error happened");
            alert.showAndWait();
        }
    }
    
    public void handlebtnUpdateAssignRoute(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignRoute.fxml"));
            Parent root = null;
            try {
                root = (Parent) loader.load();
            } catch (IOException ex) {
                LOGGER.severe("Error: "+ex.getLocalizedMessage());
            }
            FXMLDocumentAssignRouteController viewController = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Client client = ClientFactory.getClient();
            viewController.setClient(client);
            viewController.setStage(stage);
            viewController.initStage(root);
        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR, "Something went wrong opening Assign Route window, please try later or restart the programm");
            alert.setTitle("Something went wrong");
            alert.showAndWait();
            LOGGER.severe(ex.getLocalizedMessage());
        }
    }
    
    public void handlebtnReturnToMainMenu(ActionEvent e){
         try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Main_Menu.fxml"));
            Parent root = null;
            try {
                root = (Parent) loader.load();
            } catch (IOException ex) {
                LOGGER.severe("Error: "+ex.getLocalizedMessage());
            }
            Admin_Main_MenuController viewController = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            viewController.setUser(user);
            viewController.setStage(stage);
            viewController.initStage(root);
            this.stage.close();
        }catch(Exception ex){
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
            alert = new Alert(Alert.AlertType.ERROR, "Unexpected error happened");
            alert.showAndWait();
        }
    }
    
    public void handleWindowShowing(WindowEvent e){
        routeName.setText(route.getName());
        assignedTo.setText(route.getAssignedTo().getFullName());
        totalDistance.setText(route.getTotalDistance().toString());
        estimatedTime.setText(route.getEstimatedTime().toString());
        createdBy.setText(route.getCreatedBy().getFullName());
        isEnded.setSelected(route.getEnded());
        isStarted.setSelected(route.getStarted());
        mode.setValue(route.getMode());
        transportMode.setValue(route.getTransportMode());
        trafficMode.setValue(route.getTrafficMode());
        
        
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
    
    public User getUser(){
        return user;
    }
    
    public void setUser(User user){
        this.user=user;
    }
    
}
