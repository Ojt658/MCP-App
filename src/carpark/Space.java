package carpark;

import com.google.gson.annotations.Expose;
import vehicle.Vehicle;

/**
 * A class to create space objects in the zones that hold the vehicles
 * @author Ollie
 * @version 1
 */
public class Space {
    @Expose private int id;
    @Expose private boolean available;
    @Expose private Vehicle occupiedBy;

    /**
     * The space constructor to create space objects - must have an id
     * @param id becomes space id
     */
    public Space(int id) {
        this.id = id;
        available = true;
        occupiedBy = null;
    }

    /**
     * Check whether the space is available
     * @return available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Set the space to be available or not
     * @param available becomes available
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Get the space id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the vehicle occupying the space
     * @param occupiedBy becomes occupiedBy
     */
    public void setOccupiedBy(Vehicle occupiedBy) {
        this.occupiedBy = occupiedBy;
        available = false;
    }

    /**
     * Get information about the space (Empty or Full)
     * @return a String with information
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (occupiedBy != null) {
            result.append(id).append(": ").append(occupiedBy).append(" ");
        } else {
            result.append(id).append(": ").append("Empty").append(" ");
        }

        return result.toString();
    }
}