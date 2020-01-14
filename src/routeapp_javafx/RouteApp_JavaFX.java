/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx;

import beans.Route;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import routeapp_javafx.logic.Client;
import routeapp_javafx.logic.ClientFactory;
import routeapp_javafx.logic.LogicBusinessException;
import routeapp_javafx.view.FXMLDocumentAssignRouteController;
import routeapp_javafx.view.FXMLDocumentCreateRouteController;
import routeapp_javafx.view.FXMLDocumentRestorePasswordController;

/**
 *
 * @author 2dam
 */
public class RouteApp_JavaFX extends Application {
    
    @Override
    public void start(Stage stage) throws IOException  {
        /*
            Client client = ClientFactory.getClient();
            ArrayList<String> coords = new ArrayList<String>();
            coords.add("43.303920,-2.973575");
            coords.add("43.321781,-2.976150");
        try {
            client.getRoute(coords, "fastest;car;traffic:disabled", new Route());
        } catch (LogicBusinessException ex) {
            Logger.getLogger(RouteApp_JavaFX.class.getName()).log(Level.SEVERE, null, ex);
        }*/
            
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/CreateRoute.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("view/AssignRoute.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("view/RestorePassword.fxml"));
            Parent root = (Parent) loader.load();
            FXMLDocumentCreateRouteController viewController = loader.getController();
            //FXMLDocumentAssignRouteController viewController = loader.getController();
            //FXMLDocumentRestorePasswordController viewController = loader.getController();
            Client client = ClientFactory.getClient();
            viewController.setClient(client);
            viewController.setStage(stage);
            viewController.initStage(root);
            
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
