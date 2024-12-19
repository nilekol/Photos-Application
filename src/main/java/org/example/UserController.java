package org.example;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.fxml.Initializable;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;

import javafx.scene.Node;
//import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * The UserController class is responsible for displaying a users photo albums
 * and photos within those albums.
 * This class provides functionality to add an album, add a photo within an album,
 * rename an album, delete an album, delete a photo, copy and paste a photo or album,
 * move a photo, or to display details about a photo.
 * @author Nile Kolenovic and Xander Kasternakis
 */

public class UserController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    protected String username;
    protected ArrayList<PhotoAlbum> userPhotoAlbums;
    public ArrayList<String> tagCategories = new ArrayList<String>();

    //retrievedIdAndPasswords name is misleading, the idAndPasswords object is the retrieved version
    public IdAndPasswords retrievedIdAndPasswords = new IdAndPasswords();
    public IdAndPasswords idAndPasswords = retrievedIdAndPasswords.readToFile();

    @FXML
    private GridPane albumGridpane;



    @FXML
    private Button backButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField addAlbumNameTextField;

    @FXML
    private MenuItem logOutMenuItem;

    @FXML
    private MenuItem newAlbumMenuItem;

    @FXML
    private MenuItem newPhotoMenuItem;

    @FXML
    private MenuItem copyMenuItem;

    @FXML
    private MenuItem moveMenuItem;

    @FXML
    private MenuItem pasteMenuItem;

    @FXML
    private MenuItem renameMenuItem;

    @FXML
    private MenuItem deleteMenuItem;


    @FXML
    private DatePicker date1;

    @FXML
    private DatePicker date2;

    @FXML
    private Menu logOutMenu;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem quitApplicationMenuItem;

    @FXML
    private ImageView folderImage;

    @FXML
    private Button nextPage;

    @FXML
    private Button prevPage;

    @FXML
    private Label pageName;

    @FXML
    private Label homePageNumber;

    @FXML
    private Menu searchMenu;

    @FXML
    private Button searchButton;

    @FXML
    private Button createAlbum;

    @FXML
    private Label searchDateTo;

    @FXML
    private TextField searchTags;

    static int albumPage = 0;
    static int albumMaxPages = 1;

    static int photoPage = 0;
    static int photoMaxPages = 1;

    public int pageCount = 0;


    @FXML
    private ListView<PhotoAlbum> albums = new ListView<PhotoAlbum>();

    public PhotoAlbum selectedAlbum;
    public Photo selectedPhoto;

    public boolean lookingAtPhotos = false;
    public boolean lookingAtAlbums = false;

    public boolean searchByDate = false;
    public boolean searchByTags = false;
    public boolean viewingFilteredAlbum = false;

    public PhotoAlbum copiedPhotoAlbum;
    public Photo copiedPhoto;
    public Photo movedPhoto;


    /**
     * Sets the username of the user.
     * Then retrieves the users photo albums by calling retrievePhotoAlbum
     * @param username
     * @author Nile Kolenovic
     */

    public void setUsername(String username) {
        this.username = username;
        retrievePhotoAlbum();
    }


    /**
     * Retrieves the photoAlbum of the current user.
     * Sets the userPhotoAlbum to the photoAlbum retrieved
     * @author Nile Kolenovic
     *
     */

    public void retrievePhotoAlbum() {
        this.userPhotoAlbums = idAndPasswords.getPhotoAlbums(username);
    }

    boolean addButtonClicked;
    boolean renameButtonClicked;
    boolean moveButtonClicked;


    /**
     * Sets up the stock user.
     * If the stock user's photos have not been created, then the stock user's photos
     * are created and added to the stock user's photo album.
     * The stock user's photo album is then added to the userPhotoAlbums.
     * @author Nile Kolenovic
     */

    void setupStock(){
        if(!idAndPasswords.stockCreated && username.equals("stock")){

            PhotoAlbum stockAlbum = new PhotoAlbum("stock");

            for(int i = 0; i < 5; i++){
                stockAlbum.addPhotos("src/main/java/org/example/data/stock"+(i+1)+".png");
            }
            stockAlbum.setAlbumName("Stock Photos");
            userPhotoAlbums.add(stockAlbum);
            idAndPasswords.stockCreated = true;
        }



    }

    public void displayPhotos() {
        if (viewingFilteredAlbum) {
            displayPhotosOnScreen(selectedAlbum.filteredPhotos);
        }
        else {
            displayPhotosOnScreen(selectedAlbum.photos);
        }
    }

    /**
     * Displays and formats the current photos in the selectedAlbum to the user.
     * Only displays 12 at a time. nextButtonClick and prevButtonClick
     * methods handle displaying the rest of the photos.
     * The method additionally sets up action listeners to each of the
     * photos. If a photo is double clicked, it becomes the selected photo.
     * Additionally, the user gets sent to the PhotoViewer scene
     *
     * @author Nile Kolenovic and Xander Kasternakis
     */

    public void displayPhotosOnScreen(ArrayList<Photo> photos) {
        if (!viewingFilteredAlbum) {

            if(selectedAlbum.photos.size() == 1){
                pageName.setText(selectedAlbum.albumName + ": "+ selectedAlbum.photos.size() + " photo");
            }
            else{


                pageName.setText(selectedAlbum.albumName + ": "+ selectedAlbum.photos.size() + " photos");
            }
        }
        photoMaxPages = Math.max(1, (photos.size()+11)/12);
        int count = 12*photoPage;

        prevPage.setDisable(photoPage == 0);
        nextPage.setDisable(photoPage >= photoMaxPages-1);
        homePageNumber.setText("Page "+(photoPage+1)+" of "+photoMaxPages);

        lookingAtAlbums = false;
        lookingAtPhotos = true;
        albumGridpane.getChildren().clear();
        backButton.setVisible(true);
        backButton.disableProperty().set(false);
        searchMenu.disableProperty().set(false);
        newPhotoMenuItem.disableProperty().set(false);
        moveMenuItem.disableProperty().set(false);

        //write algorithm to display photos below
        for(int i = 0; i < 4; i++){
            for(int j = 0; j<4;j++){
                if(count < photos.size()){

                    String photoPath = photos.get(count).path;

                    ImageView folder = new ImageView();
                    Image image = new Image(new File(photoPath).toURI().toString());
                    folder.setImage(image);

                    folder.setFitHeight(60);
                    folder.setFitWidth(80);

                    Label label = null;

                    if(photos.get(count).caption.equals("Add Caption")){
                        label = new Label("");
                    }else{
                        label = new Label(photos.get(count).caption);
                    }


                    label.setAlignment(Pos.CENTER);

                    // Create a VBox to stack the ImageView and Label
                    VBox vbox = new VBox();
                    vbox.getChildren().addAll(folder, label);
                    vbox.setAlignment(Pos.CENTER);

                    // Add the VBox to the gridpane
                    albumGridpane.add(vbox, j, i);
                    GridPane.setHalignment(vbox, HPos.CENTER);
                    GridPane.setValignment(vbox, VPos.CENTER);

                    vbox.setOnMouseClicked(event -> {

                        int row = GridPane.getRowIndex(vbox);
                        int col = GridPane.getColumnIndex(vbox);
                        selectedPhoto = photos.get(4*row + col);


                        if(event.getClickCount() == 2){
                            //setting the scene to the PhotoViewer scene
                            try{

                                //passing the selected photo to the PhotoViewerController
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PhotoViewer.fxml"));
                                root = loader.load();
                                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root);

                                stage.setTitle(selectedPhoto.path);
                                stage.setScene(scene);
                                stage.show();

                                PhotoViewerController photoViewerController = loader.getController();
                                photoViewerController.setUserPhoto(selectedPhoto,selectedAlbum);
                                photoViewerController.username = username;
                                photoViewerController.retrievedIdAndPasswords = idAndPasswords;
                                photoViewerController.userPhotoAlbums = userPhotoAlbums;
                                if (tagCategories.size() == 0) {
                                    photoViewerController.initializeTags();
                                }
                                else {
                                    photoViewerController.tagCategories = tagCategories;
                                }
                                photoViewerController.showTags();





                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }



                        }


                    });
                }
                count++;
            }
        }


    }

    /**
     * Displays and formats the current albums in the to the user.
     * Only displays 12 at a time. nextButtonClick and prevButtonClick
     * methods handle displaying the rest of the albums.
     * The method additionally sets up action listeners to each of the
     * albums. If an album is double clicked, it becomes the selected album.
     * Additionally, the photo contents inside of that album are displayed
     * using the displayPhotosOnScreen method.
     *
     * @author Nile Kolenovic and Xander Kasternakis
     */

    public void displayAlbumsOnHomeScreen(){
        viewingFilteredAlbum = false;

        pageName.setText("Albums");
        albumMaxPages = Math.max(1, (userPhotoAlbums.size()+11)/12);
        int count = 12*albumPage;

        prevPage.setDisable(albumPage == 0);
        nextPage.setDisable(albumPage >= albumMaxPages-1);
        homePageNumber.setText("Page "+(albumPage+1)+" of "+albumMaxPages);

        lookingAtAlbums = true;
        lookingAtPhotos = false;

        searchMenu.disableProperty().set(true);
        moveMenuItem.disableProperty().set(true);

        albumGridpane.getChildren().clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (count < userPhotoAlbums.size()) {
                    // Create ImageView for the folder image
                    ImageView folder = new ImageView();
                    File file = new File("src/main/java/org/example/data/folder_image.png");

                    if (file.exists()) {
                        Image image = new Image(file.toURI().toString());
                        // Use the image as needed...
                        folder.setImage(image);
                    } else {
                        System.out.println("Image not found: " + file.getAbsolutePath());
                    }

                    

                    folder.setFitHeight(60);
                    folder.setFitWidth(80);




                    Label label = new Label(userPhotoAlbums.get(count).albumName);
                    label.setAlignment(Pos.CENTER);

                    // Create a VBox to stack the ImageView and Label
                    VBox vbox = new VBox();
                    vbox.getChildren().addAll(folder, label);
                    vbox.setAlignment(Pos.CENTER);

                    // Add the VBox to the gridpane
                    albumGridpane.add(vbox, j, i);
                    GridPane.setHalignment(vbox, HPos.CENTER);
                    GridPane.setValignment(vbox, VPos.CENTER);

                    vbox.setOnMouseClicked(event -> {

                        int row = GridPane.getRowIndex(vbox);
                        int col = GridPane.getColumnIndex(vbox);
                        selectedAlbum = userPhotoAlbums.get(4*row + col + (albumPage*12));


                        if(event.getClickCount() == 2){
                            //double click
                            displayPhotos();

                        }


                    });
                }
                count++;
            }
        }


    }


    /**
     * Handles when the user clicks the "Back Button"
     * Displays the users albums on the screen from the photo display.
     * Sets the selectedPhoto to null.
     *
     * @param event action event triggered by user
     * @author Xander Kasternakis and Nile Kolenovic
     */
    @FXML
    void backButtonClick(ActionEvent event) {
        if (viewingFilteredAlbum) {
            viewingFilteredAlbum = false;
            displayPhotos();
            searchMenu.disableProperty().set(false);
            createAlbum.setVisible(false);
        }
        else {
            selectedPhoto = null;
            albumGridpane.getChildren().clear();
            displayAlbumsOnHomeScreen();
            newPhotoMenuItem.disableProperty().set(true);
            backButton.setVisible(false);
        }
    }

    /**
     * Handles when the user clicks the copy menu item.
     * This method has different functionality depending on the state of the application.
     * If the user is looking at albums, the currently selected album will be copied.
     * This will allow for the user to paste a copy of the album
     * if the user is looking at the photos, the currently selected photo will be copied.
     * This will allow for the user to paste a copy of the photo
     * @param event action event triggered by user
     * @author Nile Kolenovic
     */

    @FXML
    void copyMenuItemClick(ActionEvent event) {
        if(lookingAtAlbums){
            copiedPhotoAlbum = selectedAlbum;
            copiedPhoto = null;
        }else if(lookingAtPhotos){
            copiedPhoto = selectedPhoto;
            copiedPhotoAlbum = null;
        }
    }

    /**Handles the event the user clicks the paste menu item.
     * This method has different functionality depending on the state of the application.
     * If the there is a copied album, and this method is triggered, the copied album is
     * pasted into the home screen. The method writes to the loginInfo.ser file.
     * If there is a copied photo, and this method is triggered, the copied photo is pasted
     * inside of the selected album. The method writes to the loginInfo.ser file.
     *
     * @param event action event triggered by the user.
     * @author Nile Kolenovic
     */

    @FXML
    void pasteMenuItemClick(ActionEvent event) {
        if(copiedPhotoAlbum != null){
            userPhotoAlbums.add(copiedPhotoAlbum);
            idAndPasswords.updatePhotoAlbums(username, userPhotoAlbums);
            displayAlbumsOnHomeScreen();
        }else if(copiedPhoto != null){

            selectedAlbum.photos.add(copiedPhoto);
            //selectedAlbum.addPhotos(copiedPhoto.path);

            idAndPasswords.updatePhotoAlbums(username, userPhotoAlbums);
            displayPhotos();
        }
    }


    /**Handles the event the user clicks the move menu item.
     * This method moves a photo from a source album
     * into the targeted album.
     * The method makes visible an input field to type in which album will be the source.
     *
     * @param event
     * @author Nile Kolenovic
     */
    @FXML
    void moveMenuItemClick(ActionEvent event) {
        if(lookingAtPhotos && selectedPhoto != null){
            copiedPhotoAlbum = selectedAlbum;
            copiedPhoto = null;



            addAlbumNameTextField.setVisible(true);
            confirmButton.setVisible(true);
            moveButtonClicked = true;

        }





    }

    /**
     * Handles the event the user clicks the new photo menu item.
     * This method allows the user to select a photo from their computer.
     * The photo selected must be of type .png, .bmp, .gif, .jpg, or .jpeg,
     * or any capitalized version of those file types.
     * If the file selected is of a valid type, the photo is added to the selected album.
     * The method writes to the loginInfo.ser file.
     * Then the photos are displayed to the user.
     * Otherwise, if the file is not of the selected format or
     * no file is selected, the program returns
     *
     * @param event action event triggered by the user
     * @author Nile Kolenovic
     */

    @FXML
    void newPhotoMenuItemClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");

        // Set extension filters for allowed image formats
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("PNG files", "*.png"),
                new ExtensionFilter("BMP files", "*.bmp"),
                new ExtensionFilter("GIF files", "*.gif"),
                new ExtensionFilter("JPEG files", "*.jpg", "*.jpeg"),
                new ExtensionFilter("PNG files", "*.PNG"),
                new ExtensionFilter("JPEG files", "*.JPG", "*.JPEG"),
                new ExtensionFilter("PNG files", "*.GIF"),
                new ExtensionFilter("PNG files", "*.BMP")




        );

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Check if the selected file has a valid extension
            String fileName = selectedFile.getName();
            if (fileName.matches(".*\\.(bmp|gif|jpg|jpeg|png|GIF|JPG|JPEG|PNG|)$")) {
                // Valid file format

                for(int i = 0; i < selectedAlbum.photos.size(); i++){
                    Photo currPhoto = selectedAlbum.photos.get(i);
                    if(currPhoto.path.equals(selectedFile.getAbsolutePath())){
                        //photo already exists in album
                        return;
                    }
                }

                selectedAlbum.addPhotos(selectedFile.getAbsolutePath());
                displayPhotos();
                idAndPasswords.updatePhotoAlbums(username, userPhotoAlbums);

            }

            else {
                // Invalid file format
                return;
            }
        } else {
            // No file selected
            return;
        }

        // Optionally, you can call displayPhotosOnScreen() here if needed.



    }

    /**
     * Handles the event the user clicks the new album menu item.
     * This method makes visible an input field to type in the name of the new album.
     * The confirm button is also displayed
     * @param event
     * @author Nile Kolenovic
     */

    @FXML
    void newAlbumMenuItemClick(ActionEvent event){
        addAlbumNameTextField.setVisible(true);
        confirmButton.setVisible(true);
        addButtonClicked = true;
    }



    /**
     * Handles the event the user clicks the rename menu item.
     * This method makes visible an input field to type in the new name of the album.
     * The confirm button is also displayed
     * @param event
     * @author Nile Kolenovic
     */
    @FXML
    void renameMenuItemClick(ActionEvent event){
        if(selectedAlbum != null){
            addAlbumNameTextField.setVisible(true);
            confirmButton.setVisible(true);
            renameButtonClicked = true;
        }


    }




    /**
     * Handles the event the user clicks the delete menu item.
     * This method has different functionality depending on the state of the application.
     * If the user is looking at albums, the currently selected album will be deleted.
     * The method writes to the loginInfo.ser file.
     * If the user is looking at photos, the currently selected photo will be deleted.
     * The method writes to the loginInfo.ser file.
     *
     * @param event action event triggered by the user
     * @author Nile Kolenovic
     */
    @FXML
    void deleteMenuItemClick(ActionEvent event){

        if(selectedAlbum != null && selectedPhoto != null && lookingAtPhotos){

            ArrayList<Photo> userPhotos = selectedAlbum.photos;

            for(int i = 0; i < userPhotoAlbums.size(); i++){
                for(int j = 0; j < userPhotoAlbums.get(i).photos.size(); j++){
                    Photo currPhoto = userPhotoAlbums.get(i).photos.get(j);
                    if(currPhoto.path.equals(selectedPhoto.path)){
                        userPhotoAlbums.get(i).photos.remove(j);

                    }


                }
            }

            idAndPasswords.updatePhotoAlbums(username, userPhotoAlbums);
            displayPhotos();
            selectedPhoto = null;
            lookingAtPhotos = false;

        }
        else if(selectedAlbum != null && selectedPhoto == null && lookingAtAlbums){
            for(int i = 0; i < userPhotoAlbums.size(); i++){
                if(userPhotoAlbums.get(i).equals(selectedAlbum)){
                    userPhotoAlbums.remove(i);

                    idAndPasswords.updatePhotoAlbums(username, userPhotoAlbums);
                    displayAlbumsOnHomeScreen();
                    selectedAlbum = null;
                    break;

                }
            }
        }



    }



    /**
     * Handles the action when the admin clicks the "Log Out" button.
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
        IdAndPasswords.writeToFile(idAndPasswords);
        Platform.exit();

    }

    /**
     * Handles the event the user clicks the "Confirm" button.
     * This method has different functionality depending on the state of the application.
     * If the Add Photo Album has been clicked, then a new Photo Album is created 
     * with the name from the input text field. Then, the new album is added to 
     * the userPhotoAlbums. Additionally, the loginInfo.ser file is updated.
     * Then the addButtonClicked would be negated.
     * If the Rename menu item has been clicked, then the method renames the selected
     * album's name to the contents of the input text field. Then the rename
     * buttonClicked would be negated.
     * Finally, if the move button has been clicked, the method will attempt
     * to find the target application with the name whatever the contents
     * are in the input text field. If the target album has been found, the selectedPhoto
     * will be moved into the target album and removed from the source album. The 
     * loginInfo.ser file is then updated.
     * Regardless of the state of the application, then the users albums
     * are displayed to the home screen. 
     * @param event action event triggered by the user.
     * @author Nile Kolenovic and Xander Kasternakis
     */


    @FXML
    void confirmButtonClick(ActionEvent event){
        String albumName = addAlbumNameTextField.getText();

        if(addButtonClicked){
            PhotoAlbum newAlbum = new PhotoAlbum(username);
            newAlbum.setAlbumName(albumName);

            userPhotoAlbums.add(newAlbum);

            albums.getItems().add(newAlbum);

            if (viewingFilteredAlbum) {
                newAlbum.photos = selectedAlbum.filteredPhotos;
            }


            idAndPasswords.updatePhotoAlbums(username, userPhotoAlbums);

            confirmButton.setVisible(false);
            addAlbumNameTextField.setText("");
            addAlbumNameTextField.setVisible(false);
            displayAlbumsOnHomeScreen();
            addButtonClicked = false;
        }else if(renameButtonClicked){
            //rename the album
            for(int i = 0; i < userPhotoAlbums.size(); i++){
                if(userPhotoAlbums.get(i).equals(selectedAlbum)){
                    userPhotoAlbums.get(i).setAlbumName(albumName);
                    idAndPasswords.updatePhotoAlbums(username, userPhotoAlbums);
                    displayAlbumsOnHomeScreen();
                    break;
                }
            }
            confirmButton.setVisible(false);
            addAlbumNameTextField.setText("");
            addAlbumNameTextField.setVisible(false);
            renameButtonClicked = false;
        }else if(moveButtonClicked){

            String targetAlbum = addAlbumNameTextField.getText();
            for(int i = 0; i < userPhotoAlbums.size(); i++){
                if(userPhotoAlbums.get(i).albumName.equals(targetAlbum)){



                    userPhotoAlbums.get(i).photos.add(selectedPhoto);


                    displayAlbumsOnHomeScreen();

                    ArrayList<Photo> userPhotos = selectedAlbum.photos;
                    for(int j = 0; j < userPhotos.size(); j++){
                        if(userPhotos.get(j).equals(selectedPhoto)){

                            movedPhoto = selectedAlbum.photos.get(j);
                            selectedAlbum.photos.remove(j);

                            lookingAtPhotos = false;
                            break;

                        }
                    }
                    idAndPasswords.updatePhotoAlbums(username, userPhotoAlbums);


                    break;
                }
            }






            confirmButton.setVisible(false);
            addAlbumNameTextField.setText("");
            addAlbumNameTextField.setVisible(false);
            moveButtonClicked = false;
            displayAlbumsOnHomeScreen();




        }




    }

    /**
     * Handles the event the user clicks the goToNextPage menu item.
     * This method has different functionality depending on the state of the application.
     * If the user is looking at the albums, the page of the album is incremented. 
     * Then, the next page of albums are displayed to the user.
     * If the user is looking at the photos, the page of the photos is incremented.
     * Then, the next page of photos are displayed to the user. 
     *
     * @param event action event triggered by the user
     * @author Xander Kasternakis
     */

    @FXML
    void goToNextPage(ActionEvent event) {
        if (lookingAtAlbums) {
            albumPage++;
            displayAlbumsOnHomeScreen();
        }
        else if (lookingAtPhotos) {
            photoPage++;
            displayPhotos();
        }
    }

    /**
     * Handles the event the user clicks the goToPrevPage menu item.
     * This method has different functionality depending on the state of the application.
     * If the user is looking at the albums, the page of the album is decremented. 
     * Then, the prev page of albums are displayed to the user.
     * If the user is looking at the photos, the page of the photos is decremented.
     * Then, the prev page of photos are displayed to the user. 
     *
     * @param event action event triggered by the user
     * @author Xander Kasternakis
     */

    @FXML
    void goToPrevPage(ActionEvent event) {
        if (lookingAtAlbums) {
            albumPage--;
            displayAlbumsOnHomeScreen();
        }
        else if (lookingAtPhotos) {
            photoPage--;
            displayPhotos();
        }
    }


    /**
     * Handles the event the user clicks the search button.
     * This method has different functionality depending on the state of the application.
     * If the user chooses to search by date, then the user is prompted to select a date range.
     * The user is then shown the photos that were taken within that date range.
     * If the user chooses to search by tags, then the user is prompted to enter a tag.
     * The user is then shown the photos that contain that tag.
     *
     *
     * @param event
     * @author XanderKasternakis
     */
    @FXML
    void searchButtonClick(ActionEvent event) {
        searchMenu.disableProperty().set(true);
        viewingFilteredAlbum = true;
        date1.setVisible(false);
        date2.setVisible(false);
        searchDateTo.setVisible(false);
        searchButton.setVisible(false);
        searchTags.setVisible(false);
        createAlbum.setVisible(true);

        if (searchByDate) {
            LocalDate fDate = date1.getValue();
            Date date = Date.from(fDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar fromDate = Calendar.getInstance();
            fromDate.setTime(date);

            LocalDate tDate = date2.getValue();
            Date date2 = Date.from(tDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(date2);

            pageName.setText(selectedAlbum.albumName+": "+getDate(fromDate)+" to "+getDate(toDate));

            for (Photo p: selectedAlbum.photos) {
                Calendar photoDate = p.date;
                photoDate.set(Calendar.HOUR_OF_DAY, 0);
                photoDate.set(Calendar.MINUTE, 0);
                photoDate.set(Calendar.SECOND, 0);
                photoDate.set(Calendar.MILLISECOND, 0);

                if (!photoDate.before(fromDate) && !photoDate.after(toDate)) {
                    selectedAlbum.filteredPhotos.add(p);
                }
            }
        }
        else if (searchByTags) {
            String tags = searchTags.getText();
            String[] tagSegsAnd = tags.split(" AND ");
            String[] tagSegsOr = tags.split(" OR ");
            if (tagSegsAnd.length == 2) {
                filterTags(tagSegsAnd[0], tagSegsAnd[1], true);
            }
            else if (tagSegsOr.length == 2) {
                filterTags(tagSegsOr[0], tagSegsOr[1], false);
            }
            else {
                filterTags(tags);
            }

            pageName.setText(selectedAlbum.albumName+": "+tags);
        }

        displayPhotos();
        searchByDate = false;
        searchByTags = false;

    }


    /**
     * Filters the photos in the selected album by the tag.
     * The photos that contain the tag are added to the filteredPhotos arraylist
     * in the selected album.
     * @param tag
     * @author Xander Kasternakis
     */
    public void filterTags(String tag) {
        for (Photo p: selectedAlbum.photos) {
            if (p.tags.contains(tag)) {
                selectedAlbum.filteredPhotos.add(p);
            }
        }
    }

    /**
     * Filters the photos in the selected album by the tags.
     * The photos that contain the tags are added to the filteredPhotos arraylist
     * in the selected album.
     *
     * @param tag1
     * @param tag2
     * @param and
     * @author Xander Kasternakis
     */

    public void filterTags(String tag1, String tag2, boolean and) {
        for (Photo p: selectedAlbum.photos) {
            if (and) {
                if (p.tags.contains(tag1) && p.tags.contains(tag2)) {
                    selectedAlbum.filteredPhotos.add(p);
                }
            }
            else {
                if (p.tags.contains(tag1) || p.tags.contains(tag2)) {
                    selectedAlbum.filteredPhotos.add(p);
                }
            }
        }
    }

    /**
     * Returns a string representation of the date.
     * @param date
     * @return String
     */

    public String getDate(Calendar date) {
        return (date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.DATE)+"/"+date.get(Calendar.YEAR);
    }


    /**
     * Handles the event the user clicks the search by date menu item.
     * This method makes visible the date pickers and the search button.
     * The searchByDate boolean is set to true.
     *
     * @param event
     * @author Xander Kasternakis
     */
    @FXML
    void searchDateMenuItemClick(ActionEvent event) {
        selectedAlbum.filteredPhotos.clear();
        date1.setVisible(true);
        date2.setVisible(true);
        searchDateTo.setVisible(true);
        searchButton.setVisible(true);

        searchByDate = true;
    }

    /**
     * Handles the event the user clicks the search by tags menu item.
     * This method makes visible the search tags input field and the search button.
     * The searchByTags boolean is set to true.
     *
     * @param event
     * @author Xander Kasternakis
     */



    @FXML
    void searchTagMenuItemClick(ActionEvent event) {
        selectedAlbum.filteredPhotos.clear();
        searchTags.setVisible(true);
        searchButton.setVisible(true);

        searchByTags = true;

    }


    /**
     * Handles the event the user clicks the create album button.
     * This method makes visible the input field and the confirm button.
     * The addButtonClicked boolean is set to true.
     * @param event
     * @author Xander Kasternakis
     */

    @FXML
    void createAlbumButtonClick(ActionEvent event) {
        addAlbumNameTextField.setVisible(true);
        confirmButton.setVisible(true);
        addButtonClicked = true;
    }
}
