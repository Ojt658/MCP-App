package vehicle;

import carpark.Zone;

/**
 * A class to create TallVehicle objects
 * @author Ollie
 * @version 1
 */
public class TallVehicle extends Vehicle implements VehicleInterface{

    /**
     * The TallVehicle constructor that sets the license
     * @param license becomes coach license
     */
    public TallVehicle (String license) {
        super(license);
        setType();
    }

    /**
     * The default TallVehicle Constructor
     */
    public TallVehicle() {
        setType();
    }

    /**
     * Necessary for the program, this returns the zones a tall vehicle can park in
     * @param zones the array of zones
     * @return zone number
     */
    @Override
    public int getZone(Zone[] zones) {
        int zone = 0;
        if (!zones[0].isFull()) {
            zone = 1;
        }
        return zone;
    }
}
