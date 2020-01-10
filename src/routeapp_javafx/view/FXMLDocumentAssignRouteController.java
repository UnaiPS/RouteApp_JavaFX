/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx.view;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import routeapp_javafx.logic.Client;
import beans.Mode;
import beans.Route;
import beans.TrafficMode;
import beans.TransportMode;
import beans.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import routeapp_javafx.logic.LogicBusinessException;

/**
 *
 * @author Daira Eguzkiza
 */
public class FXMLDocumentAssignRouteController {
    private Stage stage;
    private Client cliente;
    private User delivery = null;
    
    
    @FXML
    private Button btnSaveChanges;
    
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfEstimatedTime;
    @FXML
    private TextField tfTotalDistance;
    @FXML 
    private ComboBox cbAssignTo;
   
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the stage.
     * @param root 
     */
    public void initStage(Parent root) {
        stage.setTitle("Assign Route");
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
     * Sets everything the controller needs to be functional. For example, loads
     * the delivery users' list into the combobox.
     * @param event 
     */
    public void handleWindowShowing(WindowEvent event) {
        try {
            stage.setResizable(false);
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
        } catch (LogicBusinessException ex) {
            Logger.getLogger(FXMLDocumentAssignRouteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method is launched when the person wants to save the route. This will
     * only be saved if all the data requested is filled. The route will be sent
     * to the database to save it for it to be used later on.
     * @param event 
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event){
        if(delivery == null){
            Alert alert=new Alert(Alert.AlertType.ERROR,
                            "You haven't entered the user!!",
                            ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Route route = new Route();
        route.setAssignedTo(delivery);
         Alert alert=new Alert(Alert.AlertType.CONFIRMATION,
                            "The route has been assigned.",
                            ButtonType.OK);
            alert.showAndWait();
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
