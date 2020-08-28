package carpark;

import com.google.gson.annotations.Expose;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class to create receipt objects - it gets time and code information from the Token parent class
 *
 * @author Ollie
 * @version 1
 */
public class Receipt extends Token{

    @Expose private String location;
    @Expose private String type;

    /**
     * The constructor for creating receipt objects
     * @param code becomes code using super
     * @param type becomes type of vehicle connected to receipt
     */
    public Receipt(int code, String type) {
        super(code);
        this.type = type;
    }

    /**
     * Get the location of the parked vehicle
     * @return a String of the location of the vehicle
     */
    public String getLocation() {
        String[] splitLocation = splitLocation();
        return "Zone: " + splitLocation[1] + " Space: " + splitLocation[2];
    }

    /**
     * Get the type of vehicle the receipt is connected to
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the location of the parked vehicle in format of "Z?S?"
     * @param location becomes location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Split the encoded location to get its individual parts
     * @return the location split in a String[]
     */
    public String[] splitLocation() {
        return location.split("[ZS]");
    }

    private String getTimeString(){
        Date date = timeGiven.getTime();
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(date);
    }

    /**
     * Return information about the receipt as a String in a nice format
     * @return a String of information
     */
    @Override
    public String toString() {
        return "Receipt - Time given: " + this.getTimeString()
                + "\n        Code: " + code;
    }
}