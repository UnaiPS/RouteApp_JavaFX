/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeapp_javafx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import routeapp_javafx.logic.Client;
import routeapp_javafx.logic.ClientFactory;
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
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("view/CreateRoute.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("view/AssignRoute.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/RestorePassword.fxml"));
            Parent root = (Parent) loader.load();
            //FXMLDocumentCreateRouteController viewController = loader.getController();
            //FXMLDocumentAssignRouteController viewController = loader.getController();
            FXMLDocumentRestorePasswordController viewController = loader.getController();
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
