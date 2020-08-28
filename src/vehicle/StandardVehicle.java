package vehicle;

import carpark.Zone;

/**
 * A class to create StandardVehicle objects
 * @author Ollie
 * @version 1
 */
public class StandardVehicle extends Vehicle implements VehicleInterface{
    /**
     * The StandardVehicle constructor that sets the license
     * @param license becomes coach license
     */
    public StandardVehicle(String license) {
        super(license);
        setType();
    }

    /**
     * The default StandardVehicle constructor
     */
    public StandardVehicle() {
        setType();
    }

    /**
     * Necessary for the program, this returns the zones a standard vehicle can park in
     * @param zones the array of zones
     * @return zone number
     */
    @Override
    public int getZone(Zone[] zones) {
        int zone = 0;
        if (!zones[0].isFull()) {
            zone = 1;
        } else if (!zones[3].isFull()) {
            zone = 4;
        }
        return zone;
    }
}
