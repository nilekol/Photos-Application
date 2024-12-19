package org.example;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


/**
 * The PhotoViewerController class is responsible for displaying the photos to the user.
 * This class is also responsible for displaying the photo information to the user.
 * Photo information includes date of photo, caption of photo, and tags of photo.
 * This class also has functionality for adding tags to a photo, deleting tags from a photo,
 * and adding a caption to a photo.
 * This class also has functionality for navigating through the photos of a photo album.
 * This class also has functionality for logging out of the application and quitting the application.
 * This class also has functionality for adding a tag category.
 *
 */
public class PhotoViewerController implements Serializable{
    public Photo userPhoto;
    public String username;

    public PhotoAlbum userAlbum;
    static int photoNum;
    static int numPhotos;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView imageView;
    @FXML
    private Button backButton;

    IdAndPasswords retrievedIdAndPasswords;
    public Photo originalPhoto;

    protected ArrayList<PhotoAlbum> userPhotoAlbums;


    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem addTagMenuItem;

    @FXML
    private MenuItem deleteMenuItem;



    @FXML
    private Label captionLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ListView<String> listOfTags;
    public ArrayList<String> tagCategories = new ArrayList<String>();
    public ObservableList<String> observableList = null;

    public boolean addingTagCategory;

    @FXML
    private Button nextPhoto;

    @FXML
    private Label photoNumber;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MenuItem quitApplicationMenuItem;

    @FXML
    private Button prevPhoto;

    @FXML
    private Label tagLabel;

    @FXML
    private TextField input;

    @FXML
    private Button confirmButton;

    public boolean addingCaption = false;
    public boolean addingTag = false;
    String selectedTag = "";

    public boolean hasListener = false;

    /**
     * Initializes the tag category for the photo viewer screen.
     * The tag categories are initialized to Location, Person, and Event.
     * The user is able to add these tags of these categories to a photo.
     * The user is able to add these tags to a photo.
     * @author Nile Kolenovic
     */

    void initializeTags(){
        tagCategories.add("Location");
        tagCategories.add("Person");
        tagCategories.add("Event");

        ObservableList<String> observableList = FXCollections.observableArrayList(tagCategories);

    }

    /**
     * This method loads User album and photo page.
     * Additionally, all changes made to a photo are saved before navigating back
     * to the user album and photo page.
     * These updates are made by writing to the serializable file and setting
     * the necessary variables of the User Controller class (userPhotoAlbums, selectedAlbum, username)
     * The photos are then displayed on the screen.
     *
     * @param event action event triggered by the user
     * @throws IOException
     * @author Nile Kolenovic and Xander Kasternakis
     */

    @FXML
    void backButtonClick(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(username + "'s Photos");
        stage.setScene(scene);
        stage.show();

        UserController userController = loader.getController();

        userController.username = username;

        userController.idAndPasswords = retrievedIdAndPasswords.readToFile();

        userController.userPhotoAlbums = userPhotoAlbums;

        userController.selectedAlbum = userAlbum;

        userController.tagCategories = tagCategories;

        userController.displayPhotos();




    }

    /**
     * This method allows for a user to navigate to the next photo in a photo album
     * The user is able to navigate to the next photo by clicking the "Next Photo" button.
     * The next photo and all its information is then displayed to the user.
     *
     * @param event action event triggered by the user
     * @author Xander Kasternakis
     */

    @FXML
    void goToNextPhoto(ActionEvent event) {

        if(confirmButton.isVisible()){
            confirmButton.setVisible(false);
        }
        if(input.isVisible()){
            input.setVisible(false);
        }

        photoNum++;
        setUserPhoto(userAlbum.photos.get(photoNum-1), userAlbum);
        showTags();
    }

    /**
     * This method allows for a user to navigate to the previous photo in a photo album
     * The user is able to navigate to the previous photo by clicking the "Previous Photo" button.
     * The previous photo and all its information is then displayed to the user.
     *
     * @param event action event triggered by the user
     * @author Xander Kasternakis
     */

    @FXML
    void goToPrevPhoto(ActionEvent event) {
        if(confirmButton.isVisible()){
            confirmButton.setVisible(false);
        }
        if(input.isVisible()){
            input.setVisible(false);
        }

        photoNum--;
        setUserPhoto(userAlbum.photos.get(photoNum-1), userAlbum);
        showTags();
    }


    /**
     * Sets the current userPhoto the PhotoViewerController is displaying
     * The user is able to set the current userPhoto by passing in a Photo object and a PhotoAlbum object.
     * Additionally, the method formats the photo and its information and displays it to the user.
     *
     * @param photo photo to display
     * @param album album that photo is a part of
     * @author Xander Kasternakis
     */

    public void setUserPhoto(Photo photo, PhotoAlbum album){
        userPhoto = photo;
        String photoPath = userPhoto.path;
        userAlbum = album;
        photoNum = album.photos.indexOf(photo)+1;
        numPhotos = album.photos.size();

        prevPhoto.setDisable(photoNum == 1);
        nextPhoto.setDisable(photoNum >= numPhotos);

        imageView.setImage(new Image(new File(photoPath).toURI().toString()));
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        // Center the ImageView to top center
        StackPane.setAlignment(imageView, Pos.TOP_CENTER);

        photoNumber.setText("Photo "+photoNum+" of "+numPhotos);
        dateLabel.setText(photo.getDate());
        captionLabel.setText(photo.caption);
    }

    /**
     * This method has different functionality depending on the state of the application.
     * If the user is adding a caption to a photo, the caption is added to the photo.
     * If the user is adding a tag category, the tag category is added to the
     * listView of tagCategories the user can add to the photo
     * If the user is adding a tag, the tag is added to the photo. Then, the tags are displayed
     * to the user in the listView of tags.
     * Additionally, the method writes to the loginInfo.ser file to ensure data persistence.
     *
     * @param event action event triggered by the user
     * @author Nile Kolenovic and Xander Kasternakis
     */

    @FXML
    void confirmButtonClick(ActionEvent event) {
        if (addingCaption) {

            userPhoto.caption = input.getText();

        }
        else if (addingTag) {

            String tag = listOfTags.getSelectionModel().getSelectedItem()+ ": "+ input.getText();
            boolean tagExists = false;


            for(int i = 0; i < userPhoto.tags.size(); i++){
                if(userPhoto.tags.get(i).equals(tag)){
                    tagExists = true;
                }
            }

            if(!tagExists && listOfTags.getSelectionModel().getSelectedItem() != null){
                userPhoto.tags.add(listOfTags.getSelectionModel().getSelectedItem()+ ": "+ input.getText());
            }





            tagLabel.setText("Tags");

            listOfTags.getItems().clear();
            showTags();

            tagLabel.setText("Tags");
            addingTag = false;


        }
        else if (addingTagCategory){
            tagCategories.add(input.getText());
            addingTagCategory = false;
            tagLabel.setText("Tags");



        }
        input.setVisible(false);
        confirmButton.setVisible(false);
        addingCaption = false;
        addingTag = false;
        setUserPhoto(userPhoto, userAlbum);
        IdAndPasswords.writeToFile(retrievedIdAndPasswords);
    }

    /**
     * This method displays the tags of a photo to the user.
     * The tags are displayed in a listView.
     * Additioanlly, this method also adds a listener to the each item
     * in the listView. This listener allows for the user to select a tag.
     *
     * @author Nile Kolenovic
     */

    void showTags(){
        ObservableList<String> tagsList = FXCollections.observableArrayList(userPhoto.tags);
        listOfTags.setItems(tagsList);

        if(!hasListener){
            listOfTags.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {

                if(!addingTag){
                    selectedTag = listOfTags.getSelectionModel().getSelectedItem();
                }

            });
            hasListener = true;
        }

    }

    /**
     * Allows for a new caption to be added.
     * Sets the "Confirm" button and input field visible. Allows for the
     * confirmButtonClick method to be executed
     *
     * @param event action event triggered by the user
     * @author Xander Kasternakis
     */

    @FXML
    void addCaptionMenuItemClick(ActionEvent event) {
        input.setText("");
        addingCaption = true;
        input.setVisible(true);
        confirmButton.setVisible(true);
    }

    /**
     * Allows for a new tag category to be added.
     * Sets the "Confirm" button and input field visible. Allows for the
     * confirmButtonClick method to be executed
     * @param event action event triggered by the user
     * @author Nile Kolenovic
     */

    @FXML
    void addTagCategoryMenuItemClick(ActionEvent event){
        addingTagCategory = true;
        input.setText("");
        tagLabel.setText("Enter a new tag category: ");
        input.setVisible(true);
        confirmButton.setVisible(true);
    }


    /**
     * Allows for a new tag to be added.
     * Sets the "Confirm" button and input field visible. Allows for the
     * confirmButtonClick method to be executed.
     * Sets the listView to the current list of tag categories that the user
     * can choose from.
     * Additionally, adds an action listener to each of the listOfTags items
     *
     * @param event
     * @author Nile Kolenovic
     */
    @FXML
    void addTagMenuItemClick(ActionEvent event){
        addingTag = true;
        input.setText("");

        tagLabel.setText("Choose a tag category: ");
        observableList = FXCollections.observableArrayList(tagCategories);
        listOfTags.setItems(observableList);
        listOfTags.setDisable(false);
        listOfTags.getSelectionModel().select(0);
        listOfTags.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {

            if(addingTag){
                tagLabel.setText("Enter tag for "+newVal+": ");

                input.setVisible(true);
                confirmButton.setVisible(true);
            }

        });
    }

    /**
     * Deletes the current selected tag.
     * If the selected tag is null or is the empty string, the method
     * will return.
     * Otherwise, the selected tag will be deleted from the original photo object.
     * The program will display the remaining tags to the user and write to the
     * loginInfo ser file.
     * @param event action event preformed by the user
     * @author Nile Kolenovic
     */

    @FXML
    void deleteMenuItemClick(ActionEvent event) {

        if(selectedTag == null||selectedTag.equals("")){
            return;
        }
        else{
            userPhoto.tags.remove(selectedTag);
            setUserPhoto(userPhoto, userAlbum);
            IdAndPasswords.writeToFile(retrievedIdAndPasswords);
            selectedTag = "";
            showTags();

        }



    }
    /**
     * Kills the application.
     * Before killing the application, this method writes to the
     * loginInfo.ser file. This ensures data persistence
     * @param event action event preformed by the user
     * @author Nile Kolenovic
     */

    @FXML
    void quitApplicationMenuItemClick(ActionEvent event) {
        //quits and saves
        IdAndPasswords.writeToFile(retrievedIdAndPasswords);
        Platform.exit();

    }

    /**
     * Handles the action when the admin clicks the "Log Out" menu item.
     * User is able to log out by clicking the "Log Out" menu item.
     * The scene is changed to the Login scene.
     * @param event the action event triggered by the user
     * @throws IOException
     * @author Nile Kolenovic
     */

    @FXML
    void logOutMenuItemClick(ActionEvent event) throws IOException {
        //Switches scene to the log in screen
        //Note: because we aren't clicking a button (we are clicking a menu item), we need
        //to use stage = (Stage) menuBar.getScene().getWindow();


        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        stage = (Stage) menuBar.getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }


}
/*
 * Current bugs/Things to do:
 * 11. JavaDocs
 * 12. Make so some tag categories can only have 1 tag while others can have multiple tags
 */
