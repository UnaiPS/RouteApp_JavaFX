package view;

import client.Client;
import client.ClientFactory;
import client.ClientUser;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Privilege;
import model.Status;
import model.User;

/**
 * FXML Controller class for the Sign Up window
 *
 * @author Jon Calvo Gaminde
 */
public class FXMLDocumentControllerSignUp {

    private Logger LOGGER = Logger.getLogger("retoLogin.view.FXMLDocumentControllerSignUp");

    private Stage stage;

    private User user = new User();

    private Client client = ClientFactory.getClient();
    /*
    A-Z characters allowed
    a-z characters allowed
    0-9 numbers allowed
    email may contain only dot '.', dash '-' and underscore '_'
    rest of the characters are not allowed
     */
    private final String REGULAREXPRESSION = "^[A-Za-z0-9+_.-]+@(.+)$";
    //Set the pattern with the expression we want

    @FXML
    private TextField tfFullName;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfConfirm;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnUndo;
    @FXML
    private Button btnRedo;
    @FXML
    private Button btnRegister;

    /**
     * Initializes the stage
     *
     * @param root The Parent of the scene
     */
    public void initStage(Parent root) {
        LOGGER.info("Initializing Sign Up stage");

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Sign Up");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::handleWindowClosing);

        btnRegister.setOnAction(this::handleBtnRegister);
        btnRedo.setOnAction(this::handleBtnRedo);
        btnUndo.setOnAction(this::handleBtnUndo);
        btnCancel.setOnAction(this::handleBtnCancel);
        tfEmail.textProperty().addListener(this::textChanged);
        tfEmail.setPromptText("sarah.smith@email.com");
        tfFullName.textProperty().addListener(this::textChanged);
        tfFullName.setPromptText("Sarah Smith Summers");
        tfLogin.textProperty().addListener(this::textChanged);
        tfLogin.setPromptText("Sarah24Smith");
        pfPassword.textProperty().addListener(this::textChanged);
        pfPassword.setPromptText("Password");
        pfConfirm.textProperty().addListener(this::textChanged);
        pfConfirm.setPromptText("Repeat the password");

        stage.show();
    }

    /**
     * This method handle the actions when the user click on close button of the
     * window
     *
     * @param e Object of type WindowEvent
     */
    public void handleWindowClosing(WindowEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Close");
        alert.setHeaderText("Are you sure that you want to close the application?");
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.CANCEL) {
            e.consume();
        } else if (okButton.isPresent() && okButton.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * This method checks when text changed on the text fields or password
     * fields
     *
     * @param observable Object of type ObservableValue
     * @param oldValue Object of type String
     * @param newValue Object of type String
     */
    public void textChanged(ObservableValue observable, String oldValue, String newValue) {
        if (tfLogin.getText().trim().length() > 50) {
            showError("The Login field is too long");
            btnRegister.setDisable(true);
        } else if (tfFullName.getText().trim().length() > 85) {
            showError("The Full Name field is too long");
            btnRegister.setDisable(true);
        } else if (tfEmail.getText().trim().length() > 80) {
            showError("The Email field is too long");
            btnRegister.setDisable(true);
        } else if (pfPassword.getText().trim().length() > 200
                || pfConfirm.getText().trim().length() > 200) {
            showError("The Password field is too long");
            btnRegister.setDisable(true);
        } else if (tfEmail.getText().trim().isEmpty()
                || tfFullName.getText().trim().isEmpty()
                || tfLogin.getText().trim().isEmpty()
                || pfConfirm.getText().trim().isEmpty()
                || pfPassword.getText().trim().isEmpty()) {
            btnRegister.setDisable(true);
        } else {
            btnRegister.setDisable(false);
        }
    }

    /**
     * This method prepares the window before showing it to the user
     *
     * @param e Object of type WindowEvent
     */
    public void handleWindowShowing(WindowEvent e) {
        //Register button will be disabled
        btnRegister.setDisable(true);
        //Redo button will be disabled
        btnRedo.setDisable(true);
        //Full Name TextField will have the focus
        tfFullName.requestFocus();

        btnCancel.setMnemonicParsing(true);
        btnCancel.setText("_Cancel");

        btnUndo.setMnemonicParsing(true);
        btnUndo.setText("_Undo");

        btnRedo.setMnemonicParsing(true);
        btnRedo.setText("_Redo");

        btnRegister.setMnemonicParsing(true);
        btnRegister.setText("_Sign Up");
    }

    /**
     * This method handles the action of the cancel button
     *
     * @param e Object of type ActionEvent
     */
    public void handleBtnCancel(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Are you sure to cancel the registration?");
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.OK) {
            stage.close();
        }

    }

    /**
     * This method handles the action of the undo button
     *
     * @param e Object of type ActionEvent
     */
    public void handleBtnUndo(ActionEvent e) {
        user.setFullName(tfFullName.getText());
        user.setEmail(tfEmail.getText());
        user.setLogin(tfLogin.getText());
        tfFullName.setText("");
        tfEmail.setText("");
        tfLogin.setText("");
        pfPassword.setText("");
        pfConfirm.setText("");
        btnRedo.setDisable(false);
    }

    /**
     * This method handles the actions of the redo button
     *
     * @param e Object of type ActionEvent
     */
    public void handleBtnRedo(ActionEvent e) {
        tfFullName.setText(user.getFullName());
        tfEmail.setText(user.getEmail());
        tfLogin.setText(user.getLogin());
        btnRedo.setDisable(true);
    }

    /**
     * This method handles the actions of the register button (Sign Up)
     *
     * @param e Object of type ActionEvent
     */
    public void handleBtnRegister(ActionEvent e) {
        Pattern pattern = Pattern.compile(REGULAREXPRESSION);
        Matcher matcher = pattern.matcher(tfEmail.getText());
        Pattern patt = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher match = patt.matcher(tfLogin.getText());
        boolean specialChars = match.find();
        //Alert if the password isn't equal
        if (!pfPassword.getText().equals(pfConfirm.getText())) {
            showError("The passwords are not equal.");
        } else if (!matcher.matches()) {
            showError("The email is not valid, please enter a new one.");
        } else if (specialChars) {
            showError("The login is not valid, please enter a new one");
        } else {
            user.setFullName(tfFullName.getText());
            user.setEmail(tfEmail.getText());
            user.setLogin(tfLogin.getText());
            user.setPassword(pfPassword.getText());
            user.setLastAccess(Timestamp.from(Instant.now()));
            user.setLastPasswordChange(Timestamp.from(Instant.now()));
            user.setPrivilege(Privilege.ADMIN);
            user.setStatus(Status.ENABLED);
            try {
                client.createUser(user);
                user.setPassword(pfPassword.getText());
                user = client.login(user);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Main_Menu.fxml"));
                Parent root = null;
                try {
                    root = (Parent) loader.load();
                } catch (IOException ex) {
                    LOGGER.severe("Error: " + ex.getLocalizedMessage());
                }
                Admin_Main_MenuController viewController = loader.getController();
                viewController.setUser(user);
                stage.close();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                viewController.setStage(stage);
                viewController.initStage(root);

            } catch (Exception ex) {
                showError("Unexpected error happened.");
                LOGGER.severe("Error: " + ex.getLocalizedMessage());
            }

        }
    }

    /**
     * Setter for the stage
     *
     * @param stage Object of type Stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Getter for the stage
     *
     * @return Object of type Stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * A method that creates an error Alert with the inputed text.
     *
     * @param errorText The text to show.
     */
    private void showError(String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(errorText);
        alert.showAndWait();
    }
}
