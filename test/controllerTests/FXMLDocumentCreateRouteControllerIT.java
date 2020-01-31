package controllerTests;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import model.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.loadui.testfx.controls.TextInputControls;
import org.testfx.api.FxAssert;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.robot.Motion;
import view.FXMLDocumentControllerLogin;
import view.FXMLDocumentCreateRouteController;

/**
 * The integration test for the FXMLDocumentCreateRouteController.
 *
 * @author Daira Eguzkiza
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FXMLDocumentCreateRouteControllerIT extends ApplicationTest {

    private static boolean firstTime = true;
    private static final String USER = "Jon";
    private static final String PASSWORD = "abcd*1234";
    private static final String INVALIDROUTE = "iaugduaysgvruidwt42836trqigw";
    private static final String LONGROUTENAME = "dkufbiuapbdfiabfvibfvivfirfbie"
            + "aaasdiufbaoisdbiaoslbdfaisdfaa"
            + "duhfaildfhgioadgbiladfbviadbvi"
            + "dkhjbfliadfbipadbgadfguabgalid"
            + "dkufbiuapbdfiabfvibfvivfirfbie"
            + "gfsbdisdbgigbiadfgbadfgbgdfiuf"
            + "dljfngsdikfbnskdfbiñsfbñsidfif"
            + "dfgkjbdfibdfiabdfidbfsdkfbsdkf"
            + "dfkjnsdifbsklfdbjnslkfbjksfdjf";
    private static final String VALIDROUTE = "bilbo";

    @Override
    public void start(Stage stage) throws Exception {
        if (firstTime) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LogIn.fxml"));
            Parent root = (Parent) loader.load();
            FXMLDocumentControllerLogin viewController = loader.getController();
            viewController.setStage(stage);
            viewController.initStage(root);
            firstTime = false;
        }

    }

    /**
     * First we go to the Create Route window.
     */
    @Test
    public void testAA_GoToCreateRoute() {
        clickOn("#txtLogin");
        write(USER);
        clickOn("#txtPassword");
        write(PASSWORD);
        clickOn("#btnLogin");
        clickOn("#btnCreateRoute");
    }

    /**
     * Tests if the program won't let the user save a route if they haven't
     * filled all the fields.
     */
    @Test
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
    @Test
    public void testALongRouteName() {
        clickOn("#tfName");
        write(LONGROUTENAME);
        clickOn("#btnSaveRoute");
        verifyThat("The name you've entered is too long", isVisible());
        clickOn("Aceptar");
    }

    /**
     * Tests if the app will show an alert if the user wrote an invalid address
     * on the origin field.
     */
    @Test
    public void testBInvalidOriginRoute() {
        clickOn("#tfOrigin");
        write(INVALIDROUTE);
        clickOn("#btnCheckOrigin");
        verifyThat("We can't load data right now. Please try again later.", isVisible());
        clickOn("Aceptar");
    }

    /**
     * Tests if the app will show an alert if the user wrote an invalid address
     * on the destinations field.
     */
    @Test
    public void testBInvalidDestinationRoute() {
        clickOn("#tfDestination");
        write(INVALIDROUTE);
        clickOn("#btnCheckDestination");
        verifyThat("We can't load data right now. Please try again later.", isVisible());
        clickOn("Aceptar");
    }

    /**
     * Tests that the app will show the user the direction the external web api
     * has found.
     */
    @Test
    public void testCAValidOriginRoute() {
        clickOn("#tfOrigin");
        TextInputControls.clearTextIn("#tfOrigin");
        write(VALIDROUTE);
        clickOn("#btnCheckOrigin");
        verifyThat("Is this the direction you want?: Bilbao, Basque Country, Spain", isVisible());
        clickOn("Aceptar");
    }

    

    /**
     * Tests that the app will show the user the direction the external web api
     * has found.
     */
    @Test
    public void testCBTableShowingDest() {
        clickOn("#tfDestination");
        TextInputControls.clearTextIn("#tfDestination");
        write(VALIDROUTE);
        clickOn("#btnCheckDestination");
        verifyThat("Is this the direction you want?: Bilbao, Basque Country, Spain", isVisible());
        clickOn("Aceptar");
        FxAssert.verifyThat("#tvDestinations", TableViewMatchers.containsRow("Bilbao, Basque Country, Spain"));

    }
    
    /**
     * Tests that the app will show the user the direction the external web api
     * has found.
     */
    @Test
    public void testCCTableContextMenu() {
        moveTo("#tvDestinations");
        moveBy(-300, -160, Motion.DIRECT);
        rightClickOn();
        clickOn("Delete Row");
        FxAssert.verifyThat("#tvDestinations", TableViewMatchers.hasNumRows(1));
    }
}
