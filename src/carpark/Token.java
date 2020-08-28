package carpark;

import com.google.gson.annotations.Expose;

import java.util.Calendar;

/**
 * A class to create Token objects
 * @author Ollie
 * @version 1
 */
public class Token {
    @Expose Calendar timeGiven;
    @Expose int code;

    /**
     * The Token constructor to create the token objects
     * @param code becomes code
     */
    public Token (int code) {
        timeGiven = Calendar.getInstance(); //Get current time
        this.code = code;
    }

    /**
     * Get token code
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the time the token was given
     * @return timeGiven
     */
    public Calendar getTimeGiven() {
        return timeGiven;
    }

    /**
     * Get information about the Token object
     * @return A String of information
     */
    @Override
    public String toString() {
        return "Token - Code: " + code;
    }
}
