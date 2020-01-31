
import java.io.IOException;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import view.FXMLDocumentControllerLogin;

/**
 * The main class of the application
 *
 * @author Jon Calvo Gaminde
 */
public class Application extends javafx.application.Application {

    /**
     * The method that prepares the JavafX windows
     *
     * @param stage The stage to have the windows
     * @throws IOException Exception during the load of the login FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        Logger.getAnonymousLogger().info("Loading the login.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LogIn.fxml"));
        Parent root = (Parent) loader.load();
        FXMLDocumentControllerLogin viewController = loader.getController();
        viewController.setStage(stage);
        viewController.initStage(root);
    }

    /**
     * The method that starts the application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
