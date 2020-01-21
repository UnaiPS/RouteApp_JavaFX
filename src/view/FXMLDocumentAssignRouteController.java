/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import client.Client;
import client.ClientFactory;
import model.Route;
import model.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.LogicBusinessException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import model.Privilege;

/**
 *
 * @author Daira Eguzkiza, Unai Pérez Sánchez
 */
public class FXMLDocumentAssignRouteController {
    private Stage stage;
    private Client cliente = ClientFactory.getClient();
    private User user;
    private User delivery = null;
    private Route route;
    private Logger LOGGER = Logger.getLogger("retoLogin.view.FXMLDocumentAssignRouteController");
    
    
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
            btnSaveChanges.setOnAction(this::handleSaveButtonAction);
            tfEstimatedTime.setText(route.getEstimatedTime().toString());
            tfName.setText(route.getName());
            tfTotalDistance.setText(route.getTotalDistance().toString());
            cbAssignTo.setPromptText("Delivery man/woman");
            cbAssignTo.getItems().addAll(cliente.findUsersByPrivilege(Privilege.USER));
            cbAssignTo.setOnAction((Event ev) -> {
                delivery =
                        (User) cbAssignTo.getSelectionModel().getSelectedItem();
            });
        } catch (Exception ex) {
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
        }else{
            route.setAssignedTo(delivery);
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION,
                            "The route has been assigned.",
                            ButtonType.OK);
            alert.showAndWait();
            try{
                route.setCoordinates(null);
                cliente.editRoute(route);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Route Info.fxml"));
                Parent root = null;
                try {
                    root = (Parent) loader.load();
                } catch (IOException ex) {
                    LOGGER.severe("Error: "+ex.getLocalizedMessage());
                }
                RouteInfoController viewController = loader.getController();
                viewController.setRoute(route);
                Stage stage = new Stage();
                stage.initModality(Modality.NONE);
                viewController.setUser(user);
                viewController.setStage(stage);
                LOGGER.warning("Parent root: "+root);
                viewController.initStage(root);
                this.stage.close();
            }catch(Exception ex){
                LOGGER.severe("Error: "+ex.getLocalizedMessage());
                alert = new Alert(Alert.AlertType.ERROR, "Unexpected error happened");
                alert.showAndWait();
            }
        }
        
    }
    
    /**
     * This method handles the actions when the user click the close button of the window
     * @param event Object of type WindowEvent
     */
    public void handleWindowClosing(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Close");
        alert.setHeaderText("Are you sure that you want to discard the changes?");
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.CANCEL) {    
            event.consume();
        }else if(okButton.get()==ButtonType.YES){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Route Info.fxml"));
                Parent root = null;
                try {
                    root = (Parent) loader.load();
                } catch (IOException ex) {
                    LOGGER.severe("Error: "+ex.getLocalizedMessage());
                }
                RouteInfoController viewController = loader.getController();
                Stage stage = new Stage();
                stage.initModality(Modality.NONE);
                viewController.setUser(user);
                viewController.setRoute(route);
                viewController.setStage(stage);
                viewController.initStage(root);
                this.stage.close();
            }catch(Exception ex){
                LOGGER.severe("Error: "+ex.getLocalizedMessage());
                alert = new Alert(Alert.AlertType.ERROR, "Unexpected error happened");
                alert.showAndWait();
            }
        }
    }
    
    public Route getRoute(){
        return this.route;
    }
    
    public void setRoute(Route route){
        this.route = route;
    }
    
    public User getUser(){
        return this.user;
    }
    
    public void setUser(User user){
        this.user = user;
    }
    
}
