package view;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class for the Help window
 * @author Jon Calvo Gaminde
 */
public class FXMLDocumentControllerHelp {
    
    @FXML
    private WebView webView;
    /**
     * Opens the help window
     * @param root The Parent of the scene
     */
    public void initStage(Parent root) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle("Help");
        stage.setOnShowing(this::handleWindowShowing);
        Scene scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Loads the help html
     * @param event Object of type WindowEvent
     */
    private void handleWindowShowing(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass()
            .getResource("/help/help.html").toExternalForm());
    }
}

