package view;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import client.Client;
import client.ClientFactory;
import model.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.LogicBusinessException;

/**
 * FXML Controller class for the Restore Password window
 *
 * @author Daira Eguzkiza
 */
public class FXMLDocumentRestorePasswordController {

    private Stage stage;
    private Client cliente = ClientFactory.getClient();

    @FXML
    private Button btnRestorePassword;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfLogin;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the stage.
     *
     * @param root
     */
    public void initStage(Parent root) {
        stage.setTitle("Restore Password");
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::handleWindowClosing);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the client with the one sent from the main method.
     *
     * @param client
     */
    public void setClient(Client client) {
        cliente = client;
    }

    /**
     * Sets everything the controller needs to be functional. For example, loads
     * the delivery users' list into the combobox.
     *
     * @param event
     */
    public void handleWindowShowing(WindowEvent event) {
        stage.setResizable(false);

    }

    /**
     * This method is launched when the person wants to save the route. This
     * will only be saved if all the data requested is filled. The route will be
     * sent to the database to save it for it to be used later on.
     *
     * @param event
     */
    @FXML
    private void handleRestoreButtonAction(ActionEvent event) {
        if (tfEmail.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "You must enter the email.",
                    ButtonType.OK);
            alert.showAndWait();
        } else if (tfLogin.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "You must enter your username.",
                    ButtonType.OK);
            alert.showAndWait();
        } else {
            try {
                User user = new User();
                user.setEmail(tfEmail.getText());
                user.setLogin(tfLogin.getText());
                cliente.forgottenPassword(user);
                Logger.getLogger(FXMLDocumentRestorePasswordController.class.getName()).severe("WTF");

            } catch (Exception ex) {
                Logger.getLogger(FXMLDocumentRestorePasswordController.class.getName()).severe(ex.getLocalizedMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        ex.getLocalizedMessage(),
                        ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    /**
     * Cancels the operation and goes back to the login window.
     *
     * @param event
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        stage.close();
    }

    /**
     * This method handles the actions when the user click the close button of
     * the window
     *
     * @param event Object of type WindowEvent
     */
    public void handleWindowClosing(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Close");
        alert.setHeaderText("Are you sure that you want to close the application?");
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.CANCEL) {
            event.consume();
        } else if (okButton.isPresent() && okButton.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
