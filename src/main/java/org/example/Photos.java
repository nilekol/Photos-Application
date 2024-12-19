package org.example;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * The PhotoAssignment class is responsible for starting the application.
 * This class is also responsible for setting the title of the application.
 * This class is also responsible for setting the scene of the application.
 * This class is also responsible for setting the stage of the application.
 *
 */

public class Photos extends Application {

    /**
     * This method is responsible for starting the application.
     * @param primaryStage the stage of the application
     * @throws Exception
     * @author Nile Kolenovic
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.show();



    }
    /**
     * This method is responsible for launching the application.
     * @param args
     * @author Nile Kolenovic
     */
    public static void main(String[] args) {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        launch(args);
    }
}