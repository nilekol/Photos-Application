package org.example;

import java.io.IOException;

import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * The LoginController class is responsible for logging in the users.
 * Additionally, if a user does not have an account, there is
 * functionality for signing a user up.
 * The admin is able to access admin functionality in this class
 * @author Nile Kolenovic
 */

public class LoginController implements Serializable{

    private Stage stage;
    //private Scene scene;
    private Parent root;

    @FXML
    private Label loginStatusLabel;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordField;

    @FXML
    private Label photosLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Button signUpButton;

    IdAndPasswords retrievedIdAndPasswords = new IdAndPasswords();
    //retrievedIdAndPasswords name is misleading, the idAndPasswords object is the retrieved version
    IdAndPasswords idAndPasswords = retrievedIdAndPasswords.readToFile();



    /**
     * Handles the event a user clicks the "Sign Up" button.
     * The method gets the text from the usernameField and passwordField
     * If a user does not exist with such username and password, a
     * new user is created and is written to the loginInfo.ser file .
     * When the sign up is successful, the "Sign up success" is displayed
     * Otherwise, "Username already exists" is displayed
     * @param event the action event triggered by the user.
     * @author Nile Kolenovic
     */

    @FXML
    void signUpButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        //if the username and password is not already in the loginInfo hashmap, add it to the hashmap
        if(!idAndPasswords.checkLogin(username, password)){
            idAndPasswords.createAccount(username, password);

            //writes the loginInfo hashmap to a file stored in the data folder
            IdAndPasswords.writeToFile(idAndPasswords);


            loginStatusLabel.setText("Sign up success");
        }else{
            loginStatusLabel.setText("Username already exists");
        }

    }

    /**
     * Handles the event a user clicks the "Login" button.
     * The method gets the text from the usernameField and passwordField
     * If a user exists with such username and password, the user is logged in.\
     * Otherwise, "Login failed" is displayed.
     * If the username and password is admin, change the scene to the Administrator scene
     * @param event the action event triggered by the user.
     * @throws IOException
     * @author Nile Kolenovic
     */


    @FXML
    void loginButtonClick(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();


        if(username.equals("admin") && password.equals("admin")){
            loginStatusLabel.setText("");

            Parent root = FXMLLoader.load(getClass().getResource("/AdminMode.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Admin Mode");
            stage.setScene(scene);
            stage.show();

        }

        else if(idAndPasswords.checkLogin(username, password)){


            loginStatusLabel.setText("");
            loginStatusLabel.setText("Login successful");

            //Parent root = FXMLLoader.load(getClass().getResource("User.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User.fxml"));
            root = loader.load();

            UserController userController = loader.getController();
            //sending data to the User.fxml scene
            userController.setUsername(username);
            userController.setupStock();
            userController.displayAlbumsOnHomeScreen();


            IdAndPasswords.writeToFile(idAndPasswords);


            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setTitle(username + "'s Photos");
            stage.setScene(scene);
            stage.show();


        }else {
            loginStatusLabel.setText("Login failed");
        }
    }



}
