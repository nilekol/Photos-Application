package org.example;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The IdAndPasswords class is responsible for storing the login information for each user.
 * This class is also responsible for creating new accounts and checking if a login is valid.
 * This class also stores the photo albums for each user.
 * This class also has methods for reading and writing the login information and photo albums
 * to a file stored in the data folder.
 * This ensures data persistence between sessions.
 * This class is also responsible for creating the admin and stock accounts.
 * @author Nile Kolenovic
 */
public class IdAndPasswords implements Serializable {
    HashMap<String, String> loginInfo = new HashMap<String, String>();
    HashMap<String, ArrayList<PhotoAlbum>> photoAlbums = new HashMap<String, ArrayList<PhotoAlbum>>();
    boolean stockCreated = false;

    /**
     * The IdAndPasswords constructor is responsible for creating the admin and stock accounts.
     * In every instance of an IdAndPasswords object, the admin and stock accounts will be created.
     * This ensures that the admin and stock accounts will always be present.
     * This constructor also checks if the loginInfo.ser file exists. If it does not exist, then
     * the loginInfo.ser file will be created.
     */
    IdAndPasswords() {
        // Ensure admin and stock accounts exist
        if (!checkLogin("admin", "admin")) {
            createAccount("admin", "admin");
        }
        if (!checkLogin("stock", "stock")) {
            createAccount("stock", "stock");
        }

        // Check if the loginInfo.ser file exists
        File file = new File("src/main/java/org/example/data/loginInfo.ser");
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream("src/main/java/org/example/data/loginInfo.ser");
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {

                // Read the serialized object from the file
                IdAndPasswords data = (IdAndPasswords) in.readObject();
                this.loginInfo = data.loginInfo;  // Load login info
                this.photoAlbums = data.photoAlbums;  // Load photo albums

            } catch (Exception e) {
                System.out.println("Error reading from file: " + e.getMessage());
            }
        } else {
            // Create the file if it doesn't exist
            writeToFile(this);
        }
    }

    /**
     * This method is responsible for creating new accounts.
     * This method takes in a username and password and stores them in the loginInfo hashmap.
     * Additionally, this method creates a new arraylist of photo albums for the new user.
     * If the username or password is empty, then the method returns.
     * @param username
     * @param password
     */
    public void createAccount(String username, String password) {
        if (username.isEmpty() || password.isEmpty())
            return;

        loginInfo.put(username, password);

        ArrayList<PhotoAlbum> photoAlbum = new ArrayList<PhotoAlbum>();
        photoAlbums.put(username, photoAlbum);
    }

    /**
     * This method is responsible for checking if a login is valid.
     * This method takes in a username and password and checks if the username and password
     * are in the loginInfo hashmap. If the username and password are in the hashmap, then
     * the method returns true. If the username and password are not in the hashmap, then
     * the method returns false.
     * @param username
     * @param password
     * @return boolean
     */
    public boolean checkLogin(String username, String password) {
        if (loginInfo.containsKey(username)) {
            if (loginInfo.get(username).equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is responsible for getting a user's photo albums.
     * @param username
     * @return ArrayList<PhotoAlbum>
     */
    protected ArrayList<PhotoAlbum> getPhotoAlbums(String username) {
        return photoAlbums.get(username);
    }

    /**
     * This method is responsible for updating a user's photo albums.
     * A user may need to update their photo album when they add a new photo, delete a photo,
     * add an album, rename an album, or delete an album, or add/remove tags/captions from photos
     * @param username
     * @param newAlbum
     */
    public void updatePhotoAlbums(String username, ArrayList<PhotoAlbum> newAlbum) {
        photoAlbums.replace(username, newAlbum);
        writeToFile(this);  // Write updated data to file
    }

    /**
     * This method will write the loginInfo hashmap to a file stored in the data folder.
     * This method will update the loginInfo.ser file. This ensures data persistence.
     * If there is an error in writing to the loginInfo.ser file, then the method will print an error message.
     * @param idAndPasswords
     */
    public static void writeToFile(IdAndPasswords idAndPasswords) {

        try (FileOutputStream fileOut = new FileOutputStream("src/main/java/org/example/data/loginInfo.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(idAndPasswords);  // Write object to file
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());

        }
    }

    /**
     * This method reads the loginInfo.ser file and returns the IdAndPasswords object.
     * The IdAndPasswords object that is returned will contain all the login info (along with photo albums)
     * of all the previous users. This ensures data persistence.
     * @return IdAndPasswords
     */
    public IdAndPasswords readToFile() {
        IdAndPasswords retrievedIdAndPasswords = new IdAndPasswords();
        try (FileInputStream fileIn = new FileInputStream("src/main/java/org/example/data/loginInfo.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            retrievedIdAndPasswords = (IdAndPasswords) in.readObject();
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return retrievedIdAndPasswords;
    }
}
