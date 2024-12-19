package org.example;



import java.io.IOException;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
//import javafx.scene.control.Menu;
import java.util.ArrayList;
import java.util.Objects;


/**
 * The AdministratorController class is responsible for managing the administrative functions of the application.
 * Has functionality for adding users, deleting users, and listing all users.
 * This class also interacts with the IdAndPasswords class to read and write user login information
 * and photo albums. This ensures data persistence between sessions.
 *
 * @author Nile Kolenovic
 */

public class AdministratorController {

    private Stage stage;
    private Scene scene;
    //private Parent root;

    IdAndPasswords retrievedIdAndPasswords = new IdAndPasswords();
    IdAndPasswords idAndPasswords = retrievedIdAndPasswords.readToFile();
    HashMap<String, String> loginInfo = idAndPasswords.loginInfo;
    HashMap<String, ArrayList<PhotoAlbum>> photoAlbums = idAndPasswords.photoAlbums;

    @FXML
    private TextField addPasswordField;

    @FXML
    private Button addUserButton;

    @FXML
    private TextField addUsernameField;

    @FXML
    private Label adminLabel;

    @FXML
    private Button deleteUserButton;

    @FXML
    private TextField deleteUserField;

    @FXML
    private TextArea listAllUsersTextField;

    @FXML
    private Button listUsersButton;

    @FXML
    private Button logOutButton;

    /**
     Handles the action when the admin clicks the "Add User" button.
     Admin is able to set the new users username and password by typing in the respective text fields.
     Once this method is called, the "Add User" button changes to "Confirm".
     If the admin clicks the "Confirm" button, the new user is added.
     If the username already exists, the user is not added.
     This method also interacts with the IdAndPasswords class to write the new user to the file.

     @param event the action event triggered by the user
     @author Nile Kolenovic
     */

    @FXML
    void addUserButtonClick(ActionEvent event) {
        if(addUserButton.getText().equals( "Confirm")){
            addUserButton.setText("Add User");

            String username = addUsernameField.getText();
            String password = addPasswordField.getText();



            if(!idAndPasswords.checkLogin(username,password) && !username.isEmpty() && !password.isEmpty()){
                idAndPasswords.createAccount(username, password);
                IdAndPasswords.writeToFile(idAndPasswords);
                listAllUsersTextField.setText("User "+ username+" added with password "+ password);
            }else if(idAndPasswords.checkLogin(username,password)){
                listAllUsersTextField.setText("User already exists");
            }

            addUsernameField.visibleProperty().set(false);
            addPasswordField.visibleProperty().set(false);

        }
        else if(addUserButton.getText().equals( "Add User")){
            addUserButton.setText("Confirm");

            addUsernameField.setText("");
            addPasswordField.setText("");

            addUsernameField.visibleProperty().set(true);
            addPasswordField.visibleProperty().set(true);
        }


    }

    /**
     * Handles the action when the admin clicks the "Delete User" button.
     * Admin is able to delete a user by typing in the username of the user in the respective text field.
     * Once this method is called, the "Delete User" button changes to "Confirm".
     * If the admin clicks the "Confirm" button, the user is deleted.
     * If the user does not exist, the text "User [X] does not exist" is displayed in the listAllUsersTextField.
     * If the user is the admin or stock user, the text "Cannot delete admin or stock user" is displayed in the listAllUsersTextField.
     * This method also interacts with the IdAndPasswords class to write the new user to the file.
     *
     * @param event the action event triggered by the user
     * @author Nile Kolenovic
     */

    @FXML
    void deleteUserButtonClick(ActionEvent event) {
        if(deleteUserButton.getText().equals("Confirm")){
            String username = deleteUserField.getText();

            if(username.equals("admin") || username.equals("stock")){
                listAllUsersTextField.setText("Cannot delete admin or stock user");
                deleteUserField.visibleProperty().set(false);
                deleteUserButton.setText("Delete User");
                return;
            }
            if(username.equals("")){
                deleteUserField.visibleProperty().set(false);
                deleteUserButton.setText("Delete User");
                return;

            }



            if(!loginInfo.containsKey(username)){
                listAllUsersTextField.setText("User "+username+" does not exist");
                deleteUserField.visibleProperty().set(false);
                deleteUserButton.setText("Delete User");
                return;
            }

            loginInfo.remove(username);
            photoAlbums.remove(username);
            listAllUsersTextField.setText("User "+ username + " deleted");
            IdAndPasswords.writeToFile(idAndPasswords);

            deleteUserField.visibleProperty().set(false);
            deleteUserButton.setText("Delete User");

        }
        else if(deleteUserButton.getText().equals("Delete User")){
            deleteUserField.visibleProperty().set(true);
            deleteUserButton.setText("Confirm");

            deleteUserField.setText("");


        }
    }

    /**
     * Handles the action when the admin clicks the "List Users" button.
     * Admin is able to list all users by clicking the "List Users" button.
     * A list of all users currently in the idAndPasswords hashmap is displayed in the listAllUsersTextField.
     * @param event the action event triggered by the user
     * @author Nile Kolenovic
     */
    @FXML
    void listUsersButtonClick(ActionEvent event) {
        if(!listAllUsersTextField.getText().isEmpty())
            listAllUsersTextField.setText("");
        else
            listAllUsersTextField.setText(loginInfo.toString());
    }

    /**
     * Handles the action when the admin clicks the "Log Out" button.
     * Admin is able to log out by clicking the "Log Out" button.
     * The scene is changed to the Login scene.
     * @param event the ActionEvent triggered by the user
     * @throws IOException
     * @author Nile Kolenovic
     */

    @FXML
    void logOutButtonClick(ActionEvent event) throws IOException {
        //changes the scene to the "Login" scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

