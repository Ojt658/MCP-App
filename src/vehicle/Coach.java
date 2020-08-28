package vehicle;

import carpark.Zone;

/**
 * A Coach class to create coach objects.
 */
public class Coach extends Vehicle implements VehicleInterface{
    /**
     * The default Coach constructor
     */
    public Coach() {
        setType();
    }

    /**
     * The Coach constructor that sets the license
     * @param license becomes coach license
     */
    public Coach (String license) {
        super(license);
        setType();
    }

    /**
     * Necessary for the program, this returns the zones a coach can park in
     * @param zones the array of zones
     * @return zone number
     */
    @Override
    public int getZone(Zone[] zones) {
        int zone = 0;
        if (!zones[2].isFull()) {
            zone = 3;
        }
        return zone;
    }
}
