package org.example;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * The Photo class is responsible for storing the information for each photo.
 * This class is also responsible for storing the tags, dates, and captions for each photo.
 * This class also has methods for getting the date of a photo.
 *
 */

public class Photo implements Serializable{
    String path;
    String caption;

    //tagCategory:tag
    ArrayList<String> tags = new ArrayList<String>();

    Calendar date;

    /**
     * The constructor takes in a path and creates a new photo object.
     * The path is the file path location of the photo on the users machine
     *
     * @param path path of the photo
     * @author Nile Kolenovic and Xander Kasternakis
     */

    public Photo(String path){
        this.path = path;
        this.date = new GregorianCalendar();
        this.caption = "Add Caption";


    }

    /**
     * Gets the date of the photo object
     * The date refers to the last modification of the photo in the users machine
     * @return string date of the photo object in the format MM/DD/YYYY HH:MM AM/PM
     * @author Xander Kasternakis
     */

    public String getDate() {
        String AMorPM;
        int minute = date.get(Calendar.MINUTE);
        String min = Integer.toString(minute);
        if (minute < 10) {
            min = "0"+minute;
        }
        if (date.get(Calendar.AM_PM)==0) {
            AMorPM = "AM";
        }
        else {
            AMorPM = "PM";
        }
        return (date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.DATE)+"/"+date.get(Calendar.YEAR)+" "+date.get(Calendar.HOUR)+":"+min+" "+AMorPM;
    }

}