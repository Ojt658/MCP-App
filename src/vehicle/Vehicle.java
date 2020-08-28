package vehicle;

import com.google.gson.annotations.Expose;

/**
 * A class that creates a blueprint for all types of vehicles
 * @author Ollie
 * @version 1
 */
public abstract class Vehicle implements VehicleInterface{

    @Expose private String license;
    @Expose private String type;

    /**
     * TYhe default constructor
     */
    public Vehicle() {}

    /**
     * A constructor that sets the license
     * @param license becomes license
     */
    public Vehicle(String license) {
        this.license = license;
    }

    /**
     * A method that sets the type to equal the name of the class
     */
    void setType() {
        type = this.getClass().getName();
    }

    /**
     * Get the license number
     * @return license
     */
    public String getLicense() {
        return license;
    }

    /**
     * Set the license number
     * @param license becomes license
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * Get the type of the Vehicle
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * An equals method that checks equality by the license plates
     * @param o is the Object to be checked against
     * @return whether or not they are equal
     */
    @Override
    public boolean equals(Object o) {
        return ((Vehicle)o).getLicense().equals(this.license);
    }

    /**
     * A method that returns information about the object
     * @return a String of information
     */
    @Override
    public String toString() {
        return license;
    }

}
