package controllerTests;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableRow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.robot.Motion;
import view.FXMLDocumentCreateRouteController;
//import routeapp_javafx.RouteApp_JavaFX;

/**
 * The integration test for the FXMLDocumentCreateRouteController.
 * @author Daira Eguzkiza
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FXMLDocumentCreateRouteControllerIT extends ApplicationTest{
    private static final String INVALIDROUTE="iaugduaysgvruidwt42836trqigw";
    private static final String LONGROUTENAME="dkufbiuapbdfiabfvibfvivfirfbie"
            + "aaasdiufbaoisdbiaoslbdfaisdfaa"
            + "duhfaildfhgioadgbiladfbviadbvi"
            + "dkhjbfliadfbipadbgadfguabgalid"
            + "dkufbiuapbdfiabfvibfvivfirfbie"
            + "gfsbdisdbgigbiadfgbadfgbgdfiuf"
            + "dljfngsdikfbnskdfbiñsfbñsidfif"
            + "dfgkjbdfibdfiabdfidbfsdkfbsdkf"
            + "dfkjnsdifbsklfdbjnslkfbjksfdjf";
    private static final String VALIDROUTE="bilbo";
    
    
 
    @Override public void start(Stage stage) throws Exception {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CreateRoute.fxml"));
            Parent root = null;
                root = (Parent) loader.load();
            FXMLDocumentCreateRouteController viewController = loader.getController();
            
            User user = new User();
            //stage.initModality(Modality.NONE);
            viewController.setUser(user);
            viewController.setStage(stage);
            viewController.initStage(root);
    }
    
    /**
     * Tests if the program won't let the user save a route if they haven't 
     * filled all the fields.
     */
    //@Test
    public void testAEmptyFields() {
        clickOn("#btnSaveRoute");
        verifyThat("You must enter the route's name.", isVisible());
        clickOn("Aceptar");
        clickOn("#tfName");
        write("Route Name");
        clickOn("#btnSaveRoute");
        verifyThat("You must enter an origin.", isVisible());
        clickOn("Aceptar");
        clickOn("#tfOrigin");
        write(VALIDROUTE);
        clickOn("#btnCheckOrigin");
        verifyThat("Is this the direction you want?: Bilbao, Basque Country, Spain", isVisible());
        clickOn("Aceptar");
        clickOn("#btnSaveRoute");
        verifyThat("You must enter at least one destination.", isVisible());
        clickOn("Aceptar");
        clickOn("#tfDestination");
        write(VALIDROUTE);
        clickOn("#btnCheckDestination");
        verifyThat("Is this the direction you want?: Bilbao, Basque Country, Spain", isVisible());
        clickOn("Aceptar");
        clickOn("#btnSaveRoute");
        verifyThat("You must enter a route mode.", isVisible());
        clickOn("Aceptar");
        clickOn("#rdbtnBalanced");
        clickOn("#btnSaveRoute");
        verifyThat("You must enter your transport mode.", isVisible());
        clickOn("Aceptar");
        clickOn("#rdbtnCar");
        clickOn("#btnSaveRoute");
        verifyThat("You must enter someone to make the delivery. If you "
                                    + "don't want to do this now, please"
                                    + "check the \"Don't assign yet\" option.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests that the app will show an alert if a user tries to enter a route
     * name that is too long.
     */
    //@Test
    public void testALongRouteName() {
        clickOn("#tfName");
        write(LONGROUTENAME);
        clickOn("#btnSaveRoute");
        verifyThat("The name you've entered is too long", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests if the app will show an alert if the user wrote an invalid address on the
     * origin field.
     */
    //@Test
    public void testBInvalidOriginRoute() {
        clickOn("#tfOrigin");
        write(INVALIDROUTE);
        clickOn("#btnCheckOrigin");
        verifyThat("We couldn't find any direction with the entered data.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests if the app will show an alert if the user wrote an invalid address on the
     * destinations field.
     */
    @Test
    public void testBInvalidDestinationRoute() {
        clickOn("#tfDestination");
        write(INVALIDROUTE);
        clickOn("#btnCheckDestination");
        verifyThat("We couldn't find any direction with the entered data.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests that the app will show the user the direction the external web api has found.
     */
    @Test
    public void testCAValidOriginRoute() {
        clickOn("#tfOrigin");
        write(VALIDROUTE);
        clickOn("#btnCheckOrigin");
        verifyThat("Is this the direction you want?: Bilbao, Basque Country, Spain", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests that the app will show the user the direction the external web api has found.
     */
    //@Test
    public void testCTableShowingDest() {
        clickOn("#tfDestination");
        write(VALIDROUTE);
        clickOn("#btnCheckDestination");
        verifyThat("Is this the direction you want?: Bilbao, Basque Country, Spain", isVisible());
        clickOn("Aceptar");
        FxAssert.verifyThat("#tvDestinations", TableViewMatchers.containsRow("Bilbao, Basque Country, Spain"));
        
    }
    
    /**
     * Tests that the app will show the user the direction the external web api has found.
     */
    @Test
    public void testCTableContextMenu() {
        clickOn("#tfDestination");
        write(VALIDROUTE);
        clickOn("#btnCheckDestination");
        verifyThat("Is this the direction you want?: Bilbao, Basque Country, Spain", isVisible());
        clickOn("Aceptar");
        moveTo("#tvDestinations");
        moveBy(-300, -160, Motion.DIRECT);
        rightClickOn();
        clickOn("Delete Row");
        FxAssert.verifyThat("#tvDestinations", TableViewMatchers.hasNumRows(0));
    }
    
    
    
    
}
