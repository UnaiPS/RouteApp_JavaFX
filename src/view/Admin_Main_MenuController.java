package view;

import client.Client;
import client.ClientFactory;
import encryption.Hasher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Coordinate_Route;
import model.Route;
import model.Type;
import model.User;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class for the Admin_Main_MenuController.
 *
 * @author Unai Pérez Sánchez
 */
public class Admin_Main_MenuController {

    private Logger LOGGER = Logger.getLogger("retoLogin.view.FXMLDocumentControllerSignUp");

    private Stage stage;

    private User user = new User();

    private Route route;

    private ArrayList<Route> routes = new ArrayList<Route>();

    private Client client = ClientFactory.getClient();

    ResourceBundle properties = ResourceBundle.getBundle("clientconfig");
    private final String HERE_ID = properties.getString("hereApiId");
    private final String HERE_CODE = properties.getString("hereApiCode");

    @FXML
    private Button btnLogOut;
    @FXML
    private TableView<Route> tblRoute;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colCreatedBy;
    @FXML
    private TableColumn<?, ?> colAssignedTo;
    @FXML
    private TableColumn<?, ?> colTotalDistance;
    @FXML
    private TableColumn<?, ?> colEstimatedTime;
    @FXML
    private TableColumn<Route, Boolean> colStarted;
    @FXML
    private TableColumn<Route, Boolean> colEnded;
    @FXML
    private TableColumn<?, ?> colMode;
    @FXML
    private TableColumn<?, ?> colTransMode;
    @FXML
    private TableColumn<?, ?> colTraffMode;
    @FXML
    private Button btnRouteInfoEdit;
    @FXML
    private Button btnDeleteRoute;
    @FXML
    private Button btnDrawOnMap;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem submenuClose;
    @FXML
    private MenuItem submenuReport;
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem submenuAbout;
    @FXML
    private MenuItem submenuHIW;
    @FXML
    private Button btnEditProfile;
    @FXML
    private Text txtgreetingText;
    @FXML
    private Button btnCreateRoute;

    /**
     * Initializes the controller class.
     */
    public void initStage(Parent root) {
        LOGGER.info("Initializing Main Menu stage");

        Scene scene = new Scene(root);

        stage.setScene(scene);
        /*
        The window will not be resizable
         */
        stage.setResizable(false);
        stage.setTitle("Main Menu");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::handleWindowClosing);
        /*
        The buttons: Log Out, Edit Profile, Create Route, Route Info/Edit and
        delete Route will be enabled
         */
        btnLogOut.setOnAction(this::handleBtnLogOut);
        btnEditProfile.setOnAction(this::handleBtnProfile);
        btnCreateRoute.setOnAction(this::handleBtnCreateRoute);
        btnRouteInfoEdit.setOnAction(this::handleBtnRouteInfoEdit);
        btnDeleteRoute.setOnAction(this::handleBtnDeleteRoute);
        btnDrawOnMap.setOnAction(this::handleBtnDrawOnMap);

        submenuAbout.setOnAction(this::handleAboutMenuItem);
        submenuClose.setOnAction(this::handleCloseMenuItem);
        submenuReport.setOnAction(this::handleReportMenuItem);
        submenuHIW.setOnAction(this::handleHowItWorksMenuItem);

        colAssignedTo.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colEnded.setCellValueFactory(new PropertyValueFactory<>("ended"));
        colEstimatedTime.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
        colMode.setCellValueFactory(new PropertyValueFactory<>("mode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStarted.setCellValueFactory(new PropertyValueFactory<>("started"));
        //colStarted.setCellFactory(tc -> new CheckBoxTableCell<>());
        colTotalDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
        colTraffMode.setCellValueFactory(new PropertyValueFactory<>("trafficMode"));
        colTransMode.setCellValueFactory(new PropertyValueFactory<>("transportMode"));

        try {
            routes.addAll(client.findAllRoutes());
            ObservableList<Route> routesList = FXCollections.observableArrayList(routes);
            tblRoute.setItems(routesList);
        } catch (NullPointerException ex) {
            LOGGER.severe("No routes found.");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getLocalizedMessage());
            alert.setTitle("Something went wrong");
            alert.showAndWait();
            LOGGER.severe(ex.getLocalizedMessage());
        }

        //The window will show a greating to the user that logs in.
        txtgreetingText.setText("Welcome, " + user.getFullName());

        stage.show();
    }

    /**
     * The handler for the report menu item.
     *
     * @param event The event of clicking
     */
    public void handleReportMenuItem(ActionEvent event) {
        LOGGER.info("Print Report Menu Item pressed");
        try {
            JasperReport report
                    = JasperCompileManager.compileReport(getClass()
                            .getResourceAsStream("/view/Route_Report.jrxml"));

            JRBeanCollectionDataSource dataItems
                    = new JRBeanCollectionDataSource((Collection<Route>) tblRoute.getItems());

            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            LOGGER.severe("Error printing report: " + ex.getLocalizedMessage());
        }
    }

    /**
     * The handler for the HowItWorks menu item
     *
     * @param event The event of clicking
     */
    public void handleHowItWorksMenuItem(ActionEvent event) {
        LOGGER.info("How It Works Menu Item pressed");
    }

    /**
     * The handler for the close menu item.
     *
     * @param event The event of clicking.
     */
    public void handleCloseMenuItem(ActionEvent event) {
        LOGGER.info("Close Menu Item pressed");
        logOut();

    }

    /**
     * The handler for the about menu item.
     *
     * @param event The event of clicking.
     */
    public void handleAboutMenuItem(ActionEvent event) {
        LOGGER.info("About Menu Item pressed");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Made by Jon Calvo Gaminde, Unai Pérez Sánchez and Daira Eguzkiza Lamelas.");
        alert.setTitle("About");
        alert.setHeaderText("Version 1.0");
        Optional<ButtonType> okButton = alert.showAndWait();
        if (okButton.isPresent() && okButton.get() == ButtonType.OK) {
            alert.close();
        }
    }

    /**
     * The handler for the delete button.
     *
     * @param event The event of clicking.
     */
    public void handleBtnDeleteRoute(ActionEvent e) {
        Alert alert;
        LOGGER.info("Delete Route button pressed with the Route: ");
        try {
            if (!tblRoute.getSelectionModel().getSelectedItem().equals(null)) {
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want to delete the route?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    try {
                        client.removeRoute(tblRoute.getSelectionModel().getSelectedItem().getId().toString());
                        tblRoute.getItems().remove(tblRoute.getSelectionModel().getSelectedItem());
                        tblRoute.refresh();
                    } catch (Exception ex) {
                        alert = new Alert(Alert.AlertType.ERROR, "No route was selected. Please, select one route to edit or see the information about it.");
                        alert.setTitle("No route selected");
                        alert.showAndWait();
                        LOGGER.severe(ex.getLocalizedMessage());
                    }

                }
            } else {
                throw new Exception();
            }

        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR, "No route was selected. Please, select one route to edit or see the information about it.");
            alert.setTitle("No route selected");
            alert.showAndWait();
            LOGGER.severe(ex.getLocalizedMessage());
        }

    }

    /**
     * The handler for the draw on map button.
     *
     * @param event The event of clicking.
     */
    public void handleBtnDrawOnMap(ActionEvent e) {
        Alert alert;
        LOGGER.info("Draw Route button pressed with the Route: ");
        try {
            if (!tblRoute.getSelectionModel().getSelectedItem().equals(null)) {
                Route selectedRoute = tblRoute.getSelectionModel().getSelectedItem();
                String coords = "";
                for (Coordinate_Route coordinate : selectedRoute.getCoordinates()) {
                    coords += "waypoint" + (coordinate.getOrder() - 1) + "=" + coordinate.getCoordinate().getLatitude() + "," + coordinate.getCoordinate().getLongitude() + "&";
                    coords += "poix" + (coordinate.getOrder() - 1) + "=" + coordinate.getCoordinate().getLatitude() + "," + coordinate.getCoordinate().getLongitude() + ";";
                    if (coordinate.getCoordinate().getType().equals(Type.ORIGIN)) {
                        coords += "red;";
                    } else if (coordinate.getVisited() == null) {
                        coords += "blue;";
                    } else {
                        coords += "green;";
                    }
                    coords += "white;14;" + coordinate.getOrder() + "&";
                }
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Map");
                alert.setHeaderText("");
                Image image = new Image("https://image.maps.api.here.com/mia/1.6/routing?app_id=" + HERE_ID + "&app_code=" + HERE_CODE + "&e=Q&" + coords + "lc=1652B4&lw=6&t=0&w=800&h=600");
                ImageView imageView = new ImageView(image);
                alert.setGraphic(null);
                alert.getDialogPane().setContent(imageView);
                alert.showAndWait();
            }

        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR, "No route was selected. Please, select one route to edit or see the information about it.");
            alert.setTitle("No route selected");
            alert.showAndWait();
            LOGGER.severe(ex.getLocalizedMessage());
        }

    }

    /**
     * The handler for the route info button.
     *
     * @param event The event of clicking.
     */
    public void handleBtnRouteInfoEdit(ActionEvent e) {
        LOGGER.info("Route Info/Edit button pressed, opening new window...");
        Alert alert;
        try {
            Route selectedRoute = ((Route) tblRoute.getSelectionModel().getSelectedItem());
            if (selectedRoute == null) {
                throw new Exception();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Route Info.fxml"));
            Parent root = null;
            try {
                root = (Parent) loader.load();
            } catch (IOException ex) {
                LOGGER.severe("Error: " + ex.getLocalizedMessage());
            }
            RouteInfoController viewController = loader.getController();
            viewController.setRoute(route);
            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            viewController.setRoute(selectedRoute);
            viewController.setUser(user);
            viewController.setStage(stage);
            viewController.initStage(root);
            this.stage.close();
        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR, "No route was selected. Please, select one route to edit or see the information about it.");
            alert.setTitle("No route selected");
            alert.showAndWait();
            LOGGER.severe(ex.getLocalizedMessage());
        }

    }

    /**
     * The handler for the create route button.
     *
     * @param event The event of clicking.
     */
    public void handleBtnCreateRoute(ActionEvent e) {
        LOGGER.info("Create Route button pressed, opening new window...");
        Alert alert;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateRoute.fxml"));
            Parent root = null;
            try {
                root = (Parent) loader.load();
            } catch (IOException ex) {
                LOGGER.severe("Error: " + ex.getLocalizedMessage());
            }
            FXMLDocumentCreateRouteController viewController = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            viewController.setUser(user);
            viewController.setStage(stage);
            viewController.initStage(root);
            this.stage.close();
        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR, "Something went wrong opening Create Route window, please try later or restart the program.");
            alert.setTitle("Something went wrong");
            alert.showAndWait();
            LOGGER.severe(ex.getLocalizedMessage());
        }
    }

    /**
     * The handler for the profile button.
     *
     * @param event The event of clicking.
     */
    public void handleBtnProfile(ActionEvent e) {
        LOGGER.info("Profile button pressed, opening new window...");
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Password confirmation");
        dialog.setHeaderText("Identity confirmation by password requiered.");
        dialog.setGraphic(null);

        PasswordField pf = new PasswordField();
        pf.setPromptText("Password");

        HBox hBox = new HBox();
        hBox.getChildren().add(pf);
        hBox.setPadding(new Insets(20));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pf.getText();
            } else {
                return null;
            }
        });

        dialog.getDialogPane().setContent(hBox);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                if (Hasher.encrypt(result.get()).equals(user.getPassword())) {
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("UserProfile.fxml"));
                    Parent root = (Parent) loader.load();
                    FXMLDocumentControllerUserProfile viewController = loader.getController();
                    viewController.setUser(user);
                    viewController.setStage(stage);
                    viewController.initStage(root);
                    this.stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Incorrect password.");
                    alert.show();
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error has ocurred.");
                alert.show();
                LOGGER.severe("Error exception: " + ex.getLocalizedMessage());
            }
        }
    }

    /**
     * The handler for the log out button.
     *
     * @param event The event of clicking.
     */
    public void handleBtnLogOut(ActionEvent e) {
        LOGGER.info("Log Out button pressed, returning to the Login window...");
        logOut();
    }

    /**
     * The handler for the showing of the window.
     *
     * @param event The event of showing.
     */
    public void handleWindowShowing(WindowEvent e) {
        btnCreateRoute.setMnemonicParsing(true);
        btnCreateRoute.setText("_Create Route");

        btnDeleteRoute.setMnemonicParsing(true);
        btnDeleteRoute.setText("_Delete Route");

        btnEditProfile.setMnemonicParsing(true);
        btnEditProfile.setText("_Edit Profile");

        btnLogOut.setMnemonicParsing(true);
        btnLogOut.setText("_Log Out");

        btnRouteInfoEdit.setMnemonicParsing(true);
        btnRouteInfoEdit.setText("_Route Info/Edit");

        submenuAbout.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCodeCombination.CONTROL_ANY));
        submenuHIW.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        submenuClose.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_ANY));
        submenuReport.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_ANY));
    }

    /**
     * The handler for the window closing.
     *
     * @param event The event of closing.
     */
    public void handleWindowClosing(WindowEvent e) {
        LOGGER.info("The window was attempted to be closed");
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

    //Setters
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes.addAll(routes);
    }

    //Getters
    public Stage getStage() {
        return stage;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    /**
     * A method that do the log out of the user, deleting the client code.
     */
    public void logOut() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want to log out?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            client.setCode("");
            stage.close();
        }
    }

}
