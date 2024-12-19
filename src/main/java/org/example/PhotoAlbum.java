package org.example;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Calendar;


/**
 * The PhotoAlbum class is responsible for storing the information for each photo album.
 * This class is also responsible for storing the photos for each photo album.
 * This class also has methods for adding photos to a photo album and setting a photo album name.
 * This class also has methods for getting the photos of a photo album and displaying the photos of a photo album.
 * @author Nile Kolenovic and Xander Kasternakis
 */
public class PhotoAlbum implements Serializable{
    public String username;
    public ArrayList<Photo> photos = new ArrayList<Photo>();
    public ArrayList<Photo> filteredPhotos = new ArrayList<Photo>();
    public String albumName;

    /**
     * The constructor takes in a username and creates a new photo album object.
     * @param username username of the user
     * @author Nile Kolenovic
     */
    public PhotoAlbum(String username){
        this.username = username;
    }


    /**
     * Sets the album name of the photo album object
     * @param albumName string album name of the photo album object
     * @author Nile Kolenovic
     */
    public void setAlbumName(String albumName){
        this.albumName = albumName;
    }

    /**
     * Adds a photo to the photo album object
     * @param path path of the photo object to be added to the photo album object
     * @author Nile Kolenovic
     */
    public void addPhotos(String path){
        Photo newPhoto = new Photo(path);

        photos.add(newPhoto);
    }


    /**
     * Gets the photos that are stored in the photo album object
     * @return arraylist of photos of the photo album object
     * @author Nile Kolenovic
     */
    public ArrayList<Photo> getPhotos(){
        return photos;
    }

    /**
     * Displays the photos in the photo album object
     * @author Nile Kolenovic
     */

    public void displayPhotos(){
        for(Photo photo : photos){
            System.out.println(photo.path);
        }
    }

    /**
     * Overrirding the toString method. 
     * @Override toString method
     * @author Nile Kolenovic
     */

    public String toString(){
        return username + "'s photo album";
    }


}
