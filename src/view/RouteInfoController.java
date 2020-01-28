/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.Coordinate_Route;
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
import model.Mode;
import model.Route;
import model.TrafficMode;
import model.TransportMode;
import model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.ws.rs.NotAuthorizedException;
import model.Coordinate;
import model.Direction;
import model.FullRoute;
import model.Type;

/**
 * FXML Controller class
 *
 * @author Unai Pérez Sánchez
 */
public class RouteInfoController {
    
    private Logger LOGGER = Logger.getLogger("view.RouteInfoController");
    
    private Stage stage;
    
    private Route route;
    
    private ObservableList<Route> routes;
    
    private Client client = ClientFactory.getClient();
    
    private Alert alert;
    
    private User user;
    
    private List<Direction> directions;
    
    ResourceBundle properties = ResourceBundle.getBundle("clientconfig");
    private final String HERE_ID = properties.getString("hereApiId");
    private final String HERE_CODE = properties.getString("hereApiCode");
    
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
    private Button btnSeeOnMap;
    @FXML
    private Button btnUpdateAssingRoute;
    @FXML
    private Button btnSaveChanges;
    @FXML
    private TableView<Direction> directionsInfo;
    @FXML
    private TableColumn<Direction, Type> tblType;
    @FXML
    private TableColumn<?, ?> tblCountry;
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
        
        btnReturnToMainMenu.setOnAction(this::handlebtnReturnToMainMenu);
        btnSeeOnMap.setOnAction(this::handleBtnSeeOnMap);
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
        try {
            directions = client.findDirectionsByRoute(route.getId().toString());
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getLocalizedMessage());
            alert.setTitle("Error");
            alert.showAndWait();
            LOGGER.severe(ex.getLocalizedMessage());
        }
        
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
            client.editRoute(route);
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
            stage.initModality(Modality.NONE);
            viewController.setStage(stage);
            viewController.setRoute(route);
            viewController.setUser(user);
            viewController.initStage(root);
            this.stage.close();
            
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
        }catch(NotAuthorizedException ex){
            LOGGER.severe("Re-login requiered.");
            alert = new Alert(Alert.AlertType.ERROR, ex.getLocalizedMessage());
            alert.showAndWait();
        }catch(Exception ex){
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
            alert = new Alert(Alert.AlertType.ERROR, "Unexpected error happened");
            alert.showAndWait();
        }
    }
    
    public void handleWindowShowing(WindowEvent e){
        try{
            tblType.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getCoordinate().getType()));
            tblCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
            tblState.setCellValueFactory(new PropertyValueFactory<>("state"));
            tblCounty.setCellValueFactory(new PropertyValueFactory<>("county"));
            tblCity.setCellValueFactory(new PropertyValueFactory<>("city"));
            tblDistrict.setCellValueFactory(new PropertyValueFactory<>("district"));
            tblStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
            tblHouseNumber.setCellValueFactory(new PropertyValueFactory<>("houseNumber"));
            tblPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

            
            ObservableList<Direction> directionList = FXCollections.observableArrayList(directions);

            directionsInfo.setItems(directionList);

            routeName.setText(route.getName());
            if (route.getAssignedTo() != null) {
                assignedTo.setText(route.getAssignedTo().getFullName());
            }
            totalDistance.setText(route.getTotalDistance().toString());
            estimatedTime.setText(route.getEstimatedTime().toString());
            createdBy.setText(route.getCreatedBy().getFullName());
            isEnded.setSelected(route.getEnded());
            isStarted.setSelected(route.getStarted());
            mode.setValue(route.getMode());
            transportMode.setValue(route.getTransportMode());
            trafficMode.setValue(route.getTrafficMode());
        } catch(Exception ex) {
            LOGGER.severe(ex.getLocalizedMessage());
        }
        
        
        
    }
    
    public void handleWindowClosing(WindowEvent e){
        LOGGER.info("The window was attempted to be closed");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Close");
        alert.setHeaderText("Are you sure that you want to close the application?");
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.CANCEL) {    
            e.consume();
        } else if (okButton.isPresent() && okButton.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    public void handleBtnSeeOnMap(ActionEvent e){
        LOGGER.info("See on Map button pressed.");
        Alert alert;
        try {
            Direction selectedDirection = ((Direction)directionsInfo.getSelectionModel().getSelectedItem());
            String coords = "poix0=" + selectedDirection.getCoordinate().getLatitude()+ ","
                    + selectedDirection.getCoordinate().getLongitude() + ";blue;blue;12;%20&";
            Coordinate_Route coordRoute = route.getCoordinates().stream().filter(coor -> coor.getCoordinate().equals(selectedDirection.getCoordinate())).collect(Collectors.toList()).get(0);
            if (coordRoute.getVisited() != null) {
                LOGGER.severe("Visitado");
                LOGGER.severe(coordRoute.getVisited().toString());
                Coordinate gps = client.findCoordinate(coordRoute.getVisited().toString());
                LOGGER.severe(gps.toString());
                coords += "poix1=" + gps.getLatitude()+ "," + gps.getLongitude() + ";green;green;12;%20&";
            }
            
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Map");
            alert.setHeaderText("");
            Image image = new Image("https://image.maps.api.here.com/mia/1.6/?app_id=" + HERE_ID + "&app_code=" + HERE_CODE + "&e=Q&" + coords + "poithm=1&z=17&w=800&h=600");
            ImageView imageView = new ImageView(image);
            alert.setGraphic(null);
            alert.getDialogPane().setContent(imageView);
            alert.showAndWait();
        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR, "No direction was selected. Please, select one direction to see it on the map.");
            alert.setTitle("No direction selected");
            alert.showAndWait();
            LOGGER.severe(ex.getLocalizedMessage());
        }
        
        
    }
    
    public void setRoute(Route route){
        this.route=route;
    }
    
    public Route getRoute(){
        return this.route;
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
