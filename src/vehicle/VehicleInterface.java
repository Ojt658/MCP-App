package vehicle;

import carpark.Zone;

/**
 * An interface which should be implemented by all vehicle types.
 * It forces them to have the getZone method vital to the running of the program.
 * @author Ollie
 * @version 1
 */
interface VehicleInterface {
    /**
     * Each vehicle must have this method to show the program which zones it is allowed to park in.
     * @param zones the array of zones
     * @return the zone number to be parked in
     */
    public int getZone(Zone[] zones);
}
