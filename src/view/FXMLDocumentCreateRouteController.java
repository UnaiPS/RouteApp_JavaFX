/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import beans.DirectionTvBean;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import client.Client;
import client.ClientFactory;
import model.Direction;
import model.Mode;
import model.Route;
import model.TrafficMode;
import model.TransportMode;
import model.User;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.LogicBusinessException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import model.Privilege;

/**
 *
 * @author Daira Eguzkiza, Unai Pérez Sánchez
 */
public class FXMLDocumentCreateRouteController {
    private Logger LOGGER = Logger.getLogger("view.FXMLDocumentCreateRouteController");
    private Stage stage;
    private Client cliente = ClientFactory.getClient();
    private User delivery = null;
    private User user;
    private ArrayList<Direction> directionsJIC;
    private Direction originJIC;
    private LocalDate deadline;
    
    private ObservableList directions;
    
    final ToggleGroup groupMode = new ToggleGroup();
    
    final ToggleGroup groupTransport = new ToggleGroup();
    
    @FXML
    private DatePicker dpDeadLine = new DatePicker();
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
        
        ActionEvent event = null;
        //shortcut for saving the route pressing ctr+S
        btnSaveRoute.getScene().getAccelerators().put(
        new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), 
        new Runnable() {
          @Override public void run() {
              handleSaveButtonAction(event);
          }
        }
      );
        
        //Creation of the context menu
        ContextMenu cm = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        cm.getItems().add(delete);
        
        //Shows the context menu when clicking the table with the right mouse button
        tvDestinations.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.SECONDARY) cm.show(tvDestinations, t.getScreenX(), t.getScreenY());
            }
        });
        //deletes a table entry when clicking on the "Delete" menu item on a table row.
        delete.setOnAction(new EventHandler<ActionEvent>() {  
                @Override  
                public void handle(ActionEvent event) {  
                    handleDeleteButtonAction(event);
                }  
            });  
        btnCheckOrigin.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER)) handleOriginButtonAction(event);
            }
        });
        tfOrigin.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER)) handleOriginButtonAction(event);
            }
        });
        btnCheckDestination.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER)) handleDestinationButtonAction(event);
            }
        });
        tfDestination.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            { 
                if (ke.getCode().equals(KeyCode.ENTER)) handleDestinationButtonAction(event);
            }
        });
        cbDontAssignYet.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                   if(cbDontAssignYet.isSelected()){
                    cbDontAssignYet.setSelected(false);
                    cbAssignTo.setDisable(false);

                }else{
                    cbAssignTo.setDisable(true);
                    delivery = null;
                    cbDontAssignYet.setSelected(true);
                }
                }
            }
        });
        cbEnableTrafficMode.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                   if(cbEnableTrafficMode.isSelected()){
                    cbEnableTrafficMode.setSelected(false);
                }else cbEnableTrafficMode.setSelected(true);
                }
            }
        });
    }
    
    /**
     * Deletes the row of the table of destinations that is selected when the
     * button delete is clicked on.
     * @param event 
     */
    @FXML
    private void handleDeleteButtonAction(ActionEvent event){
        int ind = tvDestinations.getSelectionModel().getSelectedIndex();
        directionsJIC.remove(ind);
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
        try {
            Direction e = this.cliente.getDirection(tfOrigin.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
            alert.setTitle("Origin");
            alert.setHeaderText("Is this the direction you want?: " + e.getName());
            
            Optional<ButtonType> okButton = alert.showAndWait();
            if (okButton.get() == ButtonType.YES) {
                tfOriginInfo.setText(e.getName());
                originJIC = e;
            }
        } catch (LogicBusinessException ex) {
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,
                    "We can't load data right now. Please try again later.",
                    ButtonType.OK);
            alert.showAndWait();
        }catch(IndexOutOfBoundsException ex){
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,
                    "We couldn't find any direction with the entered data.",
                    ButtonType.OK);
            alert.showAndWait();
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
        Direction e = new Direction();
        try {
            e = this.cliente.getDirection(tfDestination.getText());
        } catch (LogicBusinessException ex) {
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,
                    "We can't load data right now. Please try again later.",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }catch(IndexOutOfBoundsException ex){
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,
                            "We couldn't find any direction with the entered data.",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Destination");
        alert.setHeaderText("Is this the direction you want?: " + e.getName());
        
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.get() == ButtonType.YES) {    
            tvDestinations.getItems().add(new DirectionTvBean(e.getName()));
            directionsJIC.add(e);
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
        directionsJIC = new ArrayList<Direction>();
        
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
       
        //Sets the ComboBox values as Strings so the user can only see their names.
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
        try {
            cbAssignTo.getItems().addAll(cliente.findUsersByPrivilege(Privilege.USER));
        } catch (Exception ex) {
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
        }
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
        if(tfName.getText().length()>30){
            Alert alert=new Alert(Alert.AlertType.ERROR,
                            "The name you've entered is too long",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }
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
        //I am actually making this because I had to implement a datepicker but
        //I'm not doing anything with it bc to implement it I would have to change the
        //server and at this point no es plan
        deadline = dpDeadLine.getValue();
        Instant instant = Instant.from(deadline.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        
        //Here I set the route's data.
        Route route = new Route();
        route.setCreatedBy(user);
        route.setAssignedTo(delivery);
        route.setName(tfName.getText());
        if(rdbtnBalanced.isSelected()) route.setMode(Mode.BALANCED);
        else if(rdbtnFastest.isSelected()) route.setMode(Mode.FASTEST);
        else route.setMode(Mode.SHORTEST);
        
        
        if(rdbtnCar.isSelected()) route.setTransportMode(TransportMode.CAR);
        else if(rdbtnCarHov.isSelected()) route.setTransportMode(TransportMode.CAR_HOV);
        else if(rdbtnPedestrian.isSelected()) route.setTransportMode(TransportMode.PEDESTRIAN);    
        else route.setTransportMode(TransportMode.TRUCK);
        
        
        if(cbEnableTrafficMode.isSelected()) route.setTrafficMode(TrafficMode.ENABLED);
        else route.setTrafficMode(TrafficMode.DISABLED);
        
        ArrayList<String> coords = new ArrayList<String>();
        coords.add(originJIC.getCoordinate().getLatitude() + "," + originJIC.getCoordinate().getLongitude());
        for(Direction dir : directionsJIC){
            String coord = dir.getCoordinate().getLatitude() + "," + dir.getCoordinate().getLongitude();
            coords.add(coord);
        }
        
        try {
            route = cliente.getRoute(coords, route);
            //HERE WE HAVE TO CALL THE SERVER TO SAVE THE ROUTE AND THE DIRECTIONS AND EVERYTHING.
        } catch (LogicBusinessException ex) {
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
        }
        
        Alert alert=new Alert(Alert.AlertType.INFORMATION,
                            "The route has been saved.",
                            ButtonType.OK);
        alert.showAndWait();
    }
    
    /**
     * This method handles the actions when the user click the close button of the window
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
    @FXML
    public void handleReturnToMenuAction(ActionEvent action){
        Alert alert;
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
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser(){
        return this.user;
    }
}
