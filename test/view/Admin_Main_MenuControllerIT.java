/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author Unai Pérez Sánchez
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Admin_Main_MenuControllerIT extends ApplicationTest {
    private TableView table;
    private static boolean hasBeenStarted=false;
    @Override
    public void start(Stage stage) throws Exception {
        if(!hasBeenStarted){
            hasBeenStarted=true;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
            Parent root = (Parent) loader.load();
            FXMLDocumentControllerLogin viewController = loader.getController();
            viewController.setStage(stage);
            viewController.initStage(root);
        }
        
    }
    
    @Test
    public void testA_LoginOnTheApp(){
        clickOn("#txtLogin");
        write("unai");
        clickOn("#txtPassword");
        write("abcd*1234");
        clickOn("#btnLogin");
        verifyThat("#mainMenuPane",isVisible());
    }
    
    @Test
    public void testB_CheckTableRowNotSelected(){
        clickOn("#btnDrawOnMap");
        verifyThat("No route was selected. Please, select one route to edit or see the information about it.", isVisible());
        clickOn("Aceptar");
        clickOn("#btnRouteInfoEdit");
        verifyThat("No route was selected. Please, select one route to edit or see the information about it.", isVisible());
        clickOn("Aceptar");
        clickOn("#btnDeleteRoute");
        verifyThat("No route was selected. Please, select one route to edit or see the information about it.", isVisible());
        clickOn("Aceptar");
    }
    
    @Test
    public void testC_CreateRoute(){
        table=lookup("#tblRoute").queryTableView();
        assertNotEquals("Table has no data: Cannot test.",
                        table.getItems().size(),0);
        assertEquals("Table has the size expected", table.getItems().size(),5);
        clickOn("#btnCreateRoute");
        verifyThat("#pnCreateRoute", isVisible());
        moveTo(1250,720);
        clickOn(MouseButton.PRIMARY);
        write("30/01/2020");
        clickOn("#tfName");
        write("TestFX Route");
        clickOn("#tfOrigin");
        write("Tartanga erandio");
        clickOn("#btnCheckOrigin");
        clickOn("Aceptar");
        clickOn("#tfDestination");
        write("moyua plaza bilbao");
        clickOn("#btnCheckDestination");
        clickOn("Aceptar");
        clickOn("#cbDontAssignYet");
        clickOn("#rdbtnShortest");
        clickOn("#rdbtnCar");
        clickOn("#btnSaveRoute");
        verifyThat("The route has been saved.", isVisible());
        clickOn("Aceptar");
        clickOn("#btnReturnToMenu");
        verifyThat("#mainMenuPane", isVisible());
        table=lookup("#tblRoute").queryTableView();
        assertEquals("Table hasn't the size expected", table.getItems().size(),6);
    }
    
    @Test
    public void testD_ModifyRouteCreated(){
        Node row=lookup(".table-row-cell").nth(5).query();
        assertNotNull("Row is null: table has not that row. ",row);
        clickOn(row);
        clickOn("#btnRouteInfoEdit");
        verifyThat("#routeInfoPane", isVisible());
        clickOn("#routeName");
        write("modification");
        clickOn("#btnSaveChanges");
        verifyThat("#mainMenuPane", isVisible());
    }
    
    @Test
    public void testE_DeleteRouteCreated(){
        Node row=lookup(".table-row-cell").nth(5).query();
        assertNotNull("Row is null: table has not that row. ",row);
        clickOn(row);
        clickOn("#btnDeleteRoute");
        verifyThat("Are you sure that you want to delete the route?", isVisible());
        clickOn("Sí");
        table=lookup("#tblRoute").queryTableView();
        assertEquals("Table hasn't the size expected", table.getItems().size(),5);
    }
    
    @Test
    public void testF_UserProfile(){
        clickOn("#btnEditProfile");
        verifyThat("Identity confirmation by password requiered.", isVisible());
        moveTo(950,380);
        clickOn(MouseButton.PRIMARY);
        write("abcd*1234");
        clickOn("Aceptar");
        verifyThat("#userProfilePane", isVisible());
    }
}
