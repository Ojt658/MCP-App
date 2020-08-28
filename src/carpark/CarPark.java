package carpark;

import com.google.gson.annotations.Expose;
import vehicle.Vehicle;

/**
 * A class that is used to build a car-park consisting of 5 zones
 *
 * @author Ollie
 * @version 1
 */
public class CarPark {
    private static final int numOfZones = 5;
    @Expose private Zone[] zones;

    /**
     * The constructor for creating the car-park object
     */
    public CarPark() {
        zones = new Zone[numOfZones];
        createZones();
    }

    private void createZones() {
        for (int i = 0; i < numOfZones; i++) {
            zones[i] = new Zone(i+1);
        }
    }

    /**
     * Get an array of all the zones in the car-park
     * @return zones: Zone[]
     */
    public Zone[] getZones() {
        return zones;
    }

    /**
     * Add a vehicle to the car-park
     * @param vehicle is the vehicle to be added
     * @param zoneId is the zone it should be added to
     * @return the space it has been added to
     */
    public int addVehicle(Vehicle vehicle, int zoneId) {
        return zones[zoneId-1].addVehicle(vehicle);
    }

    /**
     * Add a vehicle to the car-park
     * @param vehicle is the vehicle to be added
     * @param zone the zone where it should be added
     * @param space the space where it should be added
     * @return whether it is successful or if the space is taken
     */
    public boolean addVehicle(Vehicle vehicle, int zone, int space) {
        return zones[zone-1].addVehicle(vehicle, space);
    }

    /**
     * Remove vehicle from the car-park
     * @param zone is the zone where the vehicle is
     * @param space is the space where the vehicle is currently parked
     */
    public void removeVehicle(int zone, int space) {
        zones[zone-1].removeVehicle(space);
    }
}
