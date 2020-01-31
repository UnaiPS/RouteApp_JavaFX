package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.loadui.testfx.controls.TextInputControls;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;


/**
 * The integration test for the RouteInfoController.
 * @author Jon Calvo Gaminde
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RouteInfoControllerIT extends ApplicationTest{
    private static boolean firstTime=true;
    private static final String USER="Jon";
    private static final String PASSWORD="abcd*1234";
    
    
 
    @Override
    public void start(Stage stage) throws Exception {
        if(firstTime){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
            Parent root = (Parent) loader.load();
            FXMLDocumentControllerLogin viewController = loader.getController();
            viewController.setStage(stage);
            viewController.initStage(root);
            firstTime=false;
        }
        
    }
    
    @Test
    public void testA_GoToRouteInfo(){
        clickOn("#txtLogin");
        write(USER);
        clickOn("#txtPassword");
        write(PASSWORD);
        clickOn("#btnLogin");
        Node row=lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#btnRouteInfoEdit");
    }
    
    @Test
    public void testB_TestModes(){
        clickOn("#mode");
        clickOn("FASTEST");
        verifyThat("FASTEST", isVisible());
        clickOn("#mode");
        clickOn("BALANCED");
        verifyThat("BALANCED", isVisible());
        clickOn("#mode");
        clickOn("SHORTEST");
        verifyThat("SHORTEST", isVisible());
    }
    
    @Test
    public void testC_TestTransportModes(){
        clickOn("#transportMode");
        clickOn("CAR");
        verifyThat("CAR", isVisible());
        clickOn("#transportMode");
        clickOn("BICYCLE");
        verifyThat("BICYCLE", isVisible());
        clickOn("#transportMode");
        clickOn("CAR_HOV");
        verifyThat("CAR_HOV", isVisible());
        clickOn("#transportMode");
        clickOn("PEDESTRIAN");
        verifyThat("PEDESTRIAN", isVisible());
        clickOn("#transportMode");
        clickOn("TRUCK");
        verifyThat("TRUCK", isVisible());
    }
    
    @Test
    public void testD_TestTrafficModes(){
        clickOn("#trafficMode");
        clickOn("ENABLED");
        verifyThat("ENABLED", isVisible());
        clickOn("#trafficMode");
        clickOn("DISABLED");
        verifyThat("DISABLED", isVisible());
    }
    
    @Test
    public void testE_TestSaving(){
        clickOn("#routeName");
        TextInputControls.clearTextIn("#routeName");
        write("Testing");
        clickOn("#btnSaveChanges");
        Node row=lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#btnRouteInfoEdit");
        verifyThat("Testing", isVisible());
    }
    
    
    @Test
    public void testF_TestSeeOnMapNotSelected(){
        clickOn("#btnSeeOnMap");
        verifyThat("Error", isVisible());
        clickOn("Aceptar");
    }
    
    @Test
    public void testG_TestSeeOnMap(){
        Node row=lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#btnSeeOnMap");
        verifyThat("Aceptar", isVisible());
        clickOn("Aceptar");
    }
    
    @Test
    public void testH_TestAssign(){
        clickOn("#btnUpdateAssingRoute");
        clickOn("#cbAssignTo");
        clickOn("Repartidor Nombre");
        clickOn("#btnSaveChanges");
        clickOn("Aceptar");
        verifyThat("Repartidor Nombre", isVisible());
    }
    
    @Test
    public void testI_TestReturn(){
        clickOn("#btnReturnToMainMenu");
        verifyThat("#mainMenuPane", isVisible());
    }
    
}
