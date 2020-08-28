package carpark;

import com.google.gson.annotations.Expose;

/**
 * A class used to create Attendant objects for the car-park attendant pool
 *
 * @author Ollie
 * @version 1
 */
public class Attendant {
    @Expose private String name;
    @Expose private boolean busy;
    @Expose private int code;

    /**
     * The constructor for creating attendants - they must have a name
     * @param name becomes attendant name
     */
    public Attendant(String name) {
        this.name = name;
        busy = false;
        code = -1;
    }

    /**
     * Get the name of the attendant
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Change the name of the attendant
     * @param name becomes attendant name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check to see if attendant is busy
     * @return busy
     */
    public boolean isBusy() {
        return busy;
    }

    /**
     * Set whether the attendant is busy
     * @param busy becomes busy
     */
    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    /**
     * Gets the current attendant code - relates to the receipt code of parked vehicles
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * sets the current attendant code - relates to the receipt code of parked vehicles
     * @param code becomes code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Checks if attendants are equal by name
     * @param o is the object to be checked against
     * @return result - if they are equal
     */
    @Override
    public boolean equals(Object o) {
        boolean result;
        if (o instanceof Attendant) {
            result = ((Attendant)o).name.equals(this.name);
        } else {
            result = false;
        }
        return result;
    }
}
