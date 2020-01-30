

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import view.FXMLDocumentControllerLogin;
/**
 *
 * @author 2dam
 */
public class Application extends javafx.application.Application  {
    
    //private User user = new User();
    
    @Override
    public void start(Stage stage) throws IOException  {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LogIn.fxml"));
            Parent root = (Parent) loader.load();
            FXMLDocumentControllerLogin viewController = loader.getController();
            viewController.setStage(stage);
            viewController.initStage(root);
    }
            
    public static void main(String[] args) {
        launch(args);
    }
}
