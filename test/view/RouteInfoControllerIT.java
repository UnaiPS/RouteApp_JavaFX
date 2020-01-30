package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import javafx.stage.Stage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;


/**
 * The integration test for the RouteInfoController.
 * @author Jon Calvo
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RouteInfoControllerIT extends ApplicationTest{
    private static final String USER="username";
    private static final String PASSWORD="password";
    private static final String REALUSER="Jon";
    private static final String REALPASSWORD="abcd*1234";
    private static final String INVALIDUSERNAME="user$name·?%";
    private static final String LONGSTRING="owouwuowouwuowouwuowouwuowouwu"
            + "owouwuowouwuowouwuowouwuowouwu"
            + "owouwuowouwuowouwuowouwuowouwu"
            + "owouwuowouwuowouwuowouwuowouwu"
            + "owouwuowouwuowouwuowouwuowouwu"
            + "owouwuowouwuowouwuowouwuowouwu"
            + "owouwuowouwuowouwuowouwuowouwu"
            + "owouwuowouwuowouwuowouwuowouwu"
            + "owouwuowouwuowouwuowouwuowouwu";
    
    
 
    @Override public void start(Stage stage) throws Exception {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
            Parent root = (Parent) loader.load();
            FXMLDocumentControllerLogin viewController = loader.getController();
            viewController.setStage(stage);
            viewController.initStage(root);
    }
    
    /**
     * Tests if when you try to log in without password, the application shows
     * an alert dialog saying you must enter a password/user.
     */
    @Test
    public void testA_LoginButtonWithoutPassword() {
        clickOn("#txtLogin");
        write(REALUSER);
        clickOn("#txtPassword");
        write("");
        clickOn("#btnLogin");
        verifyThat("You must enter a username and a password.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests if when you try to log in with a non existent user the 
     * application shows an alert dialog saying that the user doesn't exist.
     */
    @Test
    public void testB_LoginButtonNonExistingUser() {
        clickOn("#txtLogin");
        write(USER);
        clickOn("#txtPassword");
        write(PASSWORD);
        clickOn("#btnLogin");
        verifyThat("No user exists for login: " + USER, isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests if when you try to log in without entering a username the 
     * application shows an alert dialog saying that you must enter a 
     * username/password.
     */
    @Test
    public void testC_LoginButtonWithoutUser() {
        clickOn("#txtLogin");
        write("");
        clickOn("#txtPassword");
        write(PASSWORD);
        clickOn("#btnLogin");
        verifyThat("You must enter a username and a password.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests if when you try to log in with an invalid username the 
     * application shows an alert dialog saying you must enter a valid
     * username.
     */
    @Test
    public void testD_LoginButtonWithInvalidUsername() {
        clickOn("#txtLogin");
        write(INVALIDUSERNAME);
        clickOn("#txtPassword");
        write(PASSWORD);
        clickOn("#btnLogin");
        verifyThat("You must enter a valid username.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests if when you try to log in with a wrong password the application
     * shows an alert dialog saying that the password is not correct.
     */
    @Test
    public void testE_LoginButtonWithWrongPassword() {
        clickOn("#txtLogin");
        write(REALUSER);
        clickOn("#txtPassword");
        write(PASSWORD);
        clickOn("#btnLogin");
        verifyThat("The entered password is wrong.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests if when you try to enter a really long username it reaches a point
     * where you can't write more, but if for some reason this fails, it must
     * show an alert dialog saying that you must enter a valid username).
     */
   @Test
    public void testF_LoginButtonWithLongUsername() {
        clickOn("#txtLogin");
        write(LONGSTRING);
        clickOn("#txtPassword");
        write(PASSWORD);
        clickOn("#btnLogin");
        if("#txtLogin".length()>30){
           verifyThat("You must enter a valid username.", isVisible());
        }
        clickOn("Aceptar");
    }
    
    /**
     * Tests if when you try to enter a really long password it reaches a point
     * where you can't write more, but if for some reason this fails, it must
     * show an alert dialog saying that you must enter a valid password).
     */
    @Test
    public void testG_LoginButtonWithLongPassword() {
        clickOn("#txtLogin");
        write(USER);
        clickOn("#txtPassword");
        write(LONGSTRING);
        clickOn("#btnLogin");
        if("#txtPassword".length()>100){
           verifyThat("You must enter a valid password.", isVisible());
        }
        clickOn("Aceptar");
    }
    
    /**
     * Tests if when you click on the sign up button, another window is opened
     * showing the sign up options.
     */
   @Test
    public void testH_SignUpBtn() {
        clickOn("#btnSignUp");
        verifyThat("Sign Up", isVisible());
        clickOn("#btnCancel");
        clickOn("Aceptar");
        
    }
    
    /**
     * Tests if when you click on the restore password button, another window is opened
     * showing the restore password options.
     */
   @Test
    public void testI_RestorePasswordBtn() {
        clickOn("#btnRestorePassword");
        verifyThat("Restore My Password", isVisible());
        clickOn("#btnCancel");
        
    }
    
    
    /** 
     * Tests if everything goes right when you enter valid parameters.
     */
    @Test
    public void testJ_LoginButtonIfOk() {
        clickOn("#txtLogin");
        write(REALUSER);
        clickOn("#txtPassword");
        write(REALPASSWORD);
        clickOn("#btnLogin");
        verifyThat("#tblRoute", isVisible());
    }
}
