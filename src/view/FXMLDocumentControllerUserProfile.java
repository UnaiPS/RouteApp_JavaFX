package view;

import client.ClientFactory;
import encryption.Hasher;
import java.io.IOException;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;
import client.Client;
import encryption.Encrypt;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * FXML Controller class for the User Profile window
 *
 * @author Jon Calvo Gaminde
 */
public class FXMLDocumentControllerUserProfile {

    private Logger LOGGER = Logger.getLogger("retoLogin.view.FXMLDocumentControllerUserProfile");

    private Stage stage;

    private User user = new User();

    private Client client = ClientFactory.getClient();

    private final String REGULAREXPRESSION = "^[A-Za-z0-9+_.-]+@(.+)$";

    @FXML
    private TextField txFullName;
    @FXML
    private TextField txEmail;
    @FXML
    private TextField txLogin;
    @FXML
    private TextField txStatus;
    @FXML
    private TextField txPrivilege;
    @FXML
    private DatePicker dpLastAccess;
    @FXML
    private DatePicker dpLastChange;
    @FXML
    private PasswordField pfNewPassword;
    @FXML
    private PasswordField pfRepeatPassword;
    @FXML
    private Button btBack;
    @FXML
    private Button btPassword;
    @FXML
    private Button btSave;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem menuItemReturn;
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem menuItemAbout;
    @FXML
    private MenuItem menuItemWorks;

    /**
     * Initializes the stage
     *
     * @param root The Parent of the scene
     */
    public void initStage(Parent root) {
        LOGGER.info("Initializing User Profile stage");

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("User Profile");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::handleWindowClosing);

        btSave.setOnAction(this::handleBtSave);
        btPassword.setOnAction(this::handleBtPassword);
        btBack.setOnAction(this::handleBtBack);
        txEmail.textProperty().addListener(this::textChanged);
        txFullName.textProperty().addListener(this::textChanged);
        txLogin.textProperty().addListener(this::textChanged);
        pfNewPassword.textProperty().addListener(this::textChanged);
        pfRepeatPassword.textProperty().addListener(this::textChanged);
        txStatus.setText(user.getStatus().toString());
        txPrivilege.setText(user.getPrivilege().toString());
        txLogin.setText(user.getLogin());
        txFullName.setText(user.getFullName());
        txEmail.setText(user.getEmail());
        dpLastAccess.setValue(LocalDate.of(user.getLastAccess().getYear() + 1900, user.getLastAccess().getMonth() + 1, user.getLastAccess().getDate()));
        dpLastChange.setValue(LocalDate.of(user.getLastPasswordChange().getYear() + 1900, user.getLastPasswordChange().getMonth() + 1, user.getLastPasswordChange().getDate()));

        menuItemAbout.setOnAction(this::handleAboutMenuItem);
        menuItemReturn.setOnAction(this::handleBtBack);
        menuItemWorks.setOnAction(this::handleHowItWorksMenuItem);

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
        if (txLogin.getText().trim().length() > 50) {
            LOGGER.warning("The Login field is too long");
            showError("The Login field is too long");
            btSave.setDisable(true);
        } else if (txFullName.getText().trim().length() > 85) {
            LOGGER.warning("The Full Name field is too long");
            showError("The Full Name field is too long");
            btSave.setDisable(true);
        } else if (txEmail.getText().trim().length() > 80) {
            LOGGER.warning("The Email field is too long");
            showError("The Email field is too long");
            btSave.setDisable(true);
        } else if (pfNewPassword.getText().trim().length() > 200
                || pfRepeatPassword.getText().trim().length() > 200) {
            LOGGER.warning("The Password field is too long");
            showError("The Password field is too long");
            btSave.setDisable(true);
        } else if (txEmail.getText().trim().isEmpty()
                || txFullName.getText().trim().isEmpty()
                || txLogin.getText().trim().isEmpty()) {
            btSave.setDisable(true);
        } else if (btPassword.isDisabled()
                && (pfNewPassword.getText().trim().isEmpty()
                || pfRepeatPassword.getText().trim().isEmpty())) {
            btSave.setDisable(true);
        } else {
            btSave.setDisable(false);
        }
    }

    /**
     * This method prepares the window before showing it to the user
     *
     * @param e Object of type WindowEvent
     */
    public void handleWindowShowing(WindowEvent e) {
        btSave.setDisable(true);
        pfNewPassword.setDisable(true);
        pfRepeatPassword.setDisable(true);
        txLogin.requestFocus();

        btBack.setMnemonicParsing(true);
        btBack.setText("_Go Back");

        btPassword.setMnemonicParsing(true);
        btPassword.setText("_Change Password");

        btSave.setMnemonicParsing(true);
        btSave.setText("_Save Changes");
    }

    /**
     * This method handles the action of the cancel button
     *
     * @param e Object of type ActionEvent
     */
    public void handleBtBack(ActionEvent e) {
        Alert alert;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Main_Menu.fxml"));
            Parent root = null;
            try {
                root = (Parent) loader.load();
            } catch (IOException ex) {
                LOGGER.severe("Error: " + ex.getLocalizedMessage());
            }
            Admin_Main_MenuController viewController = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            viewController.setUser(user);
            viewController.setStage(stage);
            viewController.initStage(root);
            this.stage.close();
        } catch (Exception ex) {
            LOGGER.severe("Error: " + ex.getLocalizedMessage());
            alert = new Alert(Alert.AlertType.ERROR, "Unexpected error happened");
            alert.showAndWait();
        }
    }

    /**
     * This method handles the action of the undo button
     *
     * @param e Object of type ActionEvent
     */
    public void handleBtPassword(ActionEvent e) {
        String emailCode = "";
        try {
            emailCode = client.emailConfirmation(user);
        } catch (Exception ex) {
            showError(ex.getLocalizedMessage());
        }
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Email confirmation");
        dialog.setHeaderText("Identity confirmation by email requiered.");
        dialog.setContentText("Insert the code that has been sended to your email:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                if (Hasher.encrypt(result.get()).equals(emailCode)) {
                    btPassword.setDisable(true);
                    btSave.setDisable(true);
                    pfNewPassword.setDisable(false);
                    pfRepeatPassword.setDisable(false);
                } else {
                    showError("Incorrect code.");
                }
            } catch (Exception ex) {
                showError("An error has ocurred.");
                LOGGER.severe("Error exception: " + ex.getLocalizedMessage());
            }
        }
    }

    /**
     * The handler for the HowItWorks menu item
     *
     * @param event The event of clicking
     */
    public void handleHowItWorksMenuItem(ActionEvent event) {
        try{
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("help.fxml"));
            root = (Parent) loader.load();
            FXMLDocumentControllerHelp viewController = loader.getController();;
            viewController.initStage(root);
        } catch (IOException ex) {
            showError("Error loading the help window.");
        }
    }

    /**
     * The handler for the about menu item.
     *
     * @param event The event of clicking.
     */
    public void handleAboutMenuItem(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Made by Jon Calvo Gaminde, Unai Pérez Sánchez and Daira Eguzkiza Lamelas.");
        alert.setTitle("About");
        alert.setHeaderText("Version 1.0");
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.OK) {
            alert.close();
        }
    }

    /**
     * This method handles the actions of the register button (Sign Up)
     *
     * @param e Object of type ActionEvent
     */
    public void handleBtSave(ActionEvent e) {
        Pattern pattern = Pattern.compile(REGULAREXPRESSION);
        Matcher matcher = pattern.matcher(txEmail.getText());
        Pattern patt = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher match = patt.matcher(txLogin.getText());
        boolean specialChars = match.find();
        //Alert if the password isn't equal
        if (btPassword.isDisabled() && !pfNewPassword.getText().equals(pfRepeatPassword.getText())) {
            LOGGER.warning("The password and the confirm password fields doesn't have the same information");
            showError("The passwords are not equal.");
        } else if (!matcher.matches()) {
            LOGGER.warning("Incorrect expression on Email field");
            showError("The email is not valid, please enter a new one.");
        } else if (specialChars) {
            LOGGER.warning("Incorrent expression on Login field");
            showError("The login is not valid, please enter a new one");
        } else {
            user.setFullName(txFullName.getText());
            user.setEmail(txEmail.getText());
            user.setLogin(txLogin.getText());
            if (btPassword.isDisabled()) {
                user.setPassword(Encrypt.cifrarTexto(pfNewPassword.getText()));
                user.setLastPasswordChange(Date.from(Instant.now()));
            }
            try {
                client.editUser(user);
                user = client.findUserByLogin(user.getLogin());
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
                showError(ex.getLocalizedMessage());
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

    //Getters
    public User getUser() {
        return user;
    }

    //Setters
    public void setUser(User user) {
        this.user = user;
    }

}
