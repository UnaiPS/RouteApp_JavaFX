/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Route;
import model.User;

/**
 * FXML Controller class
 *
 * @author Unai Pérez Sánchez
 */
public class Admin_Main_MenuController {
    
    private Logger LOGGER = Logger.getLogger("retoLogin.view.FXMLDocumentControllerSignUp");
    
    private Stage stage;
    
    private User user = new User();
    
    private Route route;
    
    private ArrayList<Route> routes = new ArrayList<Route>();
    
    
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
    private TableColumn<?, ?> colStarted;
    @FXML
    private TableColumn<?, ?> colEnded;
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
    private MenuBar menuBar;
    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem submenuClose;
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
        
        submenuAbout.setOnAction(this::handleAboutMenuItem);
        submenuClose.setOnAction(this::handleCloseMenuItem);
        submenuHIW.setOnAction(this::handleHowItWorksMenuItem);
        
        colAssignedTo.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colEnded.setCellValueFactory(new PropertyValueFactory<>("ended"));
        colEstimatedTime.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
        colMode.setCellValueFactory(new PropertyValueFactory<>("mode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStarted.setCellValueFactory(new PropertyValueFactory<>("started"));
        colTotalDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
        colTraffMode.setCellValueFactory(new PropertyValueFactory<>("trafficMode"));
        colTransMode.setCellValueFactory(new PropertyValueFactory<>("transportMode"));
        //routes.stream().forEach(route->);
        ObservableList<Route> routesList = FXCollections.observableArrayList(routes);
        
        tblRoute.setItems(routesList);
        
        /*
        The window will show a greating to the user that logs in
        */
        txtgreetingText.setText("Welcome, "+user.getFullName());
        
        stage.show();
    }
    
    public void handleHowItWorksMenuItem(ActionEvent event){
        LOGGER.info("How It Works Menu Item pressed");
    }
    
    public void handleCloseMenuItem(ActionEvent event){
        LOGGER.info("Close Menu Item pressed");
    }
    
    public void handleAboutMenuItem(ActionEvent event){
        LOGGER.info("About Menu Item pressed");
    }
    
    public void handleBtnDeleteRoute(ActionEvent e){
        LOGGER.info("Delete Route button pressed with the Route: ");
    }
    
    public void handleBtnRouteInfoEdit(ActionEvent e){
        LOGGER.info("Route Info/Edit button pressed, opening new window...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Route Info.fxml"));
        Parent root = null;
        try {
            root = (Parent) loader.load();
        } catch (IOException ex) {
            LOGGER.severe("Error: "+ex.getLocalizedMessage());
        }
        RouteInfoController viewController = loader.getController();
        viewController.setRoute(route);
        stage.close();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        viewController.setRoute(route);
        viewController.setStage(stage);
        viewController.initStage(root);
    }
    
    public void handleBtnCreateRoute(ActionEvent e){
        LOGGER.info("Create Route button pressed, opening new window...");
    }
    
    public void handleBtnProfile(ActionEvent e){
        LOGGER.info("Profile button pressed, opening new window...");
    }
    
    public void handleBtnLogOut(ActionEvent e){
        LOGGER.info("Log Out button pressed, returning to the Login window...");
    }
    
    public void handleWindowShowing(WindowEvent e){
        btnCreateRoute.setMnemonicParsing(true);
        btnCreateRoute.setText("_Create Route");
        
        btnDeleteRoute.setMnemonicParsing(true);
        btnDeleteRoute.setText("_Delete Route");
        
        btnEditProfile.setMnemonicParsing(true);
        btnEditProfile.setText("_Edit Profile");
        
        btnLogOut.setMnemonicParsing(true);
        btnLogOut.setText("_Log Out");
        
        btnRouteInfoEdit.setMnemonicParsing(true);
        btnRouteInfoEdit.setText("_¨Route Info/Edit");
        
        submenuAbout.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCodeCombination.ALT_ANY));
        submenuHIW.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        submenuClose.setAccelerator(new KeyCodeCombination(KeyCode.C,KeyCodeCombination.ALT_ANY));
    }
    
    public void handleWindowClosing(WindowEvent e){
        LOGGER.info("The window was attempted to be closed");
    }
    
    /**
     * Setter for the stage
     * @param stage Object of type Stage
     */
    public void setStage(Stage stage){
        this.stage=stage;
    }
    /**
     * Getter for the stage
     * @return Object of type Stage
     */
    public Stage getStage(){
        return stage;
    }
    
    public void setUser(User user){
        this.user = user;
    }
    
    public User getUser(){
        return user;
    }
    
    public void setRoutes(ArrayList<Route> routes){
        this.routes.addAll(routes);
    }
    
    public ArrayList<Route> getRoutes(){
        return routes;
    }
    
}
