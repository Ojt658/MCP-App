package vehicle;

import carpark.Zone;

/**
 * A class to create LongVehicle objects
 * @author Ollie
 * @version 1
 */
public class LongVehicle extends Vehicle implements VehicleInterface {
    /**
     * The LongVehicle constructor that sets the license
     * @param license becomes coach license
     */
    public LongVehicle(String license) {
        super(license);
        setType();
    }

    /**
     * The default LongVehicle constructor
     */
    public LongVehicle() {
        setType();
    }

    /**
     * Necessary for the program, this returns the zones a long vehicle can park in
     * @param zones the array of zones
     * @return zone number
     */
    @Override
    public int getZone(Zone[] zones) {
        int zone = 0;
        if (!zones[1].isFull()) {
            zone = 2;
        }
        return zone;
    }
}
