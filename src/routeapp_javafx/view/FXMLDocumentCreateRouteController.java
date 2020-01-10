/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx.view;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import routeapp_javafx.logic.Client;
import routeapp_javafx.logic.Direction;
import routeapp_javafx.logic.Mode;
import routeapp_javafx.logic.Route;
import routeapp_javafx.logic.TrafficMode;
import routeapp_javafx.logic.TransportMode;
import routeapp_javafx.logic.User;

/**
 *
 * @author Daira Eguzkiza
 */
public class FXMLDocumentCreateRouteController {
    private Stage stage;
    private Client cliente;
    private User delivery = null;
    
    private ObservableList directions;
    
    private DirectionTvManager directionsManager;
    
    @FXML
    private Button btnReturnToMenu;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSaveRoute;
    
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfOrigin;
    @FXML
    private Button btnCheckOrigin;
    @FXML
    private TextField tfDestination;
    @FXML
    private Button btnCheckDestination;
    @FXML
    private ComboBox cbAssignTo;
    @FXML
    private CheckBox cbDontAssignYet;
    @FXML
    private RadioButton rdbtnFastest;
    @FXML
    private RadioButton rdbtnShortest;
    @FXML
    private RadioButton rdbtnBalanced;
    @FXML
    private RadioButton rdbtnCar;
    @FXML
    private RadioButton rdbtnCarHov;
    @FXML
    private RadioButton rdbtnPedestrian;
    @FXML
    private RadioButton rdbtnTruck;
    @FXML
    private CheckBox cbEnableTrafficMode;
    @FXML
    private TextField tfOriginInfo;
    @FXML
    private TableView tvDestinations;
    @FXML
    private TableColumn tcName;
    
    final ToggleGroup groupMode = new ToggleGroup();
    
    final ToggleGroup groupTransport = new ToggleGroup();
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the stage.
     * @param root 
     */
    public void initStage(Parent root) {
        stage.setTitle("Create Route");
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::handleWindowClosing);
        Scene scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
        
    }
    
    /**
     * Sets the client with the one sent from the main method.
     * @param client 
     */
    public void setClient(Client client) {
        cliente = client;
     }
    
    /**
     * Deletes the row of the table of destinations that is selected when the
     * button delete is clicked on.
     * @param event 
     */
    @FXML
    private void handleDeleteButtonAction(ActionEvent event){
        tvDestinations.getItems().remove(tvDestinations.getSelectionModel()
                .getSelectedItem());
        tvDestinations.refresh();
    }
    
    /**
     * Searches for the direction that is requested and asks the user if the
     * direction that the web sevice has found is the one they wanted. If it is
     * the data will be loaded on the disabled "Origin" textfield. 
     * @param event 
     */
    @FXML
    private void handleOriginButtonAction(ActionEvent event){
        Direction e = this.cliente.getDirection(tfOrigin.getText());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Origin");
        alert.setHeaderText("Is this the direction you want?: " + e.getName());
        
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.CANCEL) {    
            //nada
        }else{
            tfOriginInfo.setText(e.getName());
        }
        
    }
    
    /**
     * Searches for the direction that is requested and asks the user if the
     * direction that the web sevice has found is the one they wanted. If it is
     * the data will be loaded on the destinations table.
     * @param event 
     */
    @FXML
    private void handleDestinationButtonAction(ActionEvent event){
        Direction e = this.cliente.getDirection(tfDestination.getText());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Destination");
        alert.setHeaderText("Is this the direction you want?: " + e.getName());
        
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.CANCEL) {    
            //nada
        }else{
           tvDestinations.getItems().add(new DirectionTvBean(e.getName()));
        }
        tvDestinations.refresh();
    }
    
    /**
     * Sets everything the controller needs to be functional. For example, loads
     * the delivery users' list into the combobox.
     * @param event 
     */
    public void handleWindowShowing(WindowEvent event) {
        stage.setResizable(false);
        rdbtnFastest.setToggleGroup(groupMode);
        rdbtnBalanced.setToggleGroup(groupMode);
        rdbtnShortest.setToggleGroup(groupMode);
        
        rdbtnCar.setToggleGroup(groupTransport);
        rdbtnCarHov.setToggleGroup(groupTransport);
        rdbtnPedestrian.setToggleGroup(groupTransport);
        rdbtnTruck.setToggleGroup(groupTransport);

        cbDontAssignYet.selectedProperty().addListener(new ChangeListener<Boolean>() {
            /**
             * Manages whether the "don't assign yet" option is checked. If it's
             * checked, the combobox will be disabled so the user can't pick one.
             * Otherwise, the delivery property will be set to null.
             * @param observable
             * @param oldValue
             * @param newValue 
             */
        @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cbDontAssignYet.isSelected()){
                    cbAssignTo.setDisable(true);
                    delivery = null;
                }else{
                    cbAssignTo.setDisable(false);
                }
            }
        });
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
       
        //sets the ComboBox values as Strings so the user can only see their names.
        cbAssignTo.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User object) {
               return object.getFullName();
            }

            @Override
            public User fromString(String string) {
                return null;
            }
    });
        cbAssignTo.setPromptText("Delivery man/woman");
        cbAssignTo.getItems().addAll(cliente.getDeliveryUsers());
        cbAssignTo.setOnAction((Event ev) -> {
            delivery = 
                (User) cbAssignTo.getSelectionModel().getSelectedItem();    
        });
    }
    
    /**
     * This method is launched when the person wants to save the route. This will
     * only be saved if all the data requested is filled. The route will be sent
     * to the database to save it for it to be used later on.
     * @param event 
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event){
        if("".equals(tfName.getText())){
            Alert alert=new Alert(Alert.AlertType.ERROR,
                            "You must enter the route's name.",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }else if("".equals(tfOriginInfo.getText())){
             Alert alert=new Alert(Alert.AlertType.ERROR,
                            "You must enter an origin.",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }else if(tvDestinations.getItems() == null || tvDestinations.getItems()
                .isEmpty()){
             Alert alert=new Alert(Alert.AlertType.ERROR,
                            "You must enter at least one destination.",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }else if(!rdbtnBalanced.isSelected() && !rdbtnFastest
                .isSelected() && !rdbtnShortest.isSelected()){
             Alert alert=new Alert(Alert.AlertType.ERROR,
                            "You must enter a route mode.",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }else if(!rdbtnCar.isSelected() && !rdbtnCarHov.isSelected() && 
                !rdbtnPedestrian.isSelected() && !rdbtnTruck.isSelected()){
             Alert alert=new Alert(Alert.AlertType.ERROR,
                            "You must enter your transport mode.",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }else if(delivery==null && !cbDontAssignYet.isSelected()){
            Alert alert=new Alert(Alert.AlertType.ERROR,
                            "You must enter someone to make the delivery. If you "
                                    + "don't want to do this now, please"
                                    + "check the \"Don't assign yet\" option.",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Route route = new Route();
        route.setAssignedTo(delivery);
        route.setName(tfName.getText());
        if(rdbtnBalanced.isSelected()){
            route.setMode(Mode.BALANCED);
        }else if(rdbtnFastest.isSelected()){
            route.setMode(Mode.FASTEST);
        }else{
            route.setMode(Mode.SHORTEST);
        }
        
        if(rdbtnCar.isSelected()){
            route.setTransportMode(TransportMode.CAR);
        }else if(rdbtnCarHov.isSelected()){
            route.setTransportMode(TransportMode.CAR_HOV);
        }else if(rdbtnPedestrian.isSelected()){
            route.setTransportMode(TransportMode.PEDESTRIAN);    
        }else{
            route.setTransportMode(TransportMode.TRUCK);
        }
        if(cbEnableTrafficMode.isSelected()){
            route.setTrafficMode(TrafficMode.ENABLED);
        }
        
    }
    
    /**
     * This method handle the actions when the user click the close button of the window
     * @param event Object of type WindowEvent
     */
    public void handleWindowClosing(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Close");
        alert.setHeaderText("Are you sure that you want to close the application?");
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.CANCEL) {    
            event.consume();
        }
    }
    
    
    
}
