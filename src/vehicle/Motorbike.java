package vehicle;

import carpark.Zone;

/**
 * A class to create Motorbike objects
 * @author Ollie
 * @version 1
 */
public class Motorbike extends Vehicle implements VehicleInterface{
    /**
     * The Motorbike constructor that sets the license
     * @param license becomes coach license
     */
    public Motorbike(String license) {
        super(license);
        setType();
    }

    /**
     * The default Motorbike constructor
     */
    public Motorbike() {
        setType();
    }

    /**
     * Necessary for the program, this returns the zones a motorbike can park in
     * @param zones the array of zones
     * @return zone number
     */
    @Override
    public int getZone(Zone[] zones) {
        int zone = 0;
        if (!zones[4].isFull()) {
            zone = 5;
        }
        return zone;
    }
}
