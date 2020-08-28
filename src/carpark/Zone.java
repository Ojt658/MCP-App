package carpark;

import com.google.gson.annotations.Expose;
import vehicle.Vehicle;

/**
 * A class that is used to build the different zones for the car-park
 *
 * @author Ollie
 * @version 1
 */
public class Zone {
    @Expose private final int numOfSpaces = 10;
    @Expose private int id;
    @Expose private Space[] spaces;
    @Expose private boolean isFull;

    /**
     * The constructor for building zone objects
     * @param id becomes id
     */
    public Zone(int id) {
        spaces = new Space[numOfSpaces];
        initialiseSpaces();
        this.id = id;
        isFull = false;
    }

    /**
     * Checks if the zone is full
     * @return isFull
     */
    public boolean isFull() {
        return isFull;
    }

    private void initialiseSpaces() {
        for (int i = 0; i < numOfSpaces; i++) {
            spaces[i] = new Space(i+1);
        }
    }

    /**
     * Adds a vehicle to the zone
     * @param vehicle is the vehicle added
     * @return the spaceId it was added to
     */
    public int addVehicle(Vehicle vehicle) {
        int spaceId = findFreeSpace();
        if (spaceId != 0) {
            Space space = spaces[spaceId - 1];
            space.setOccupiedBy(vehicle);
        }
        return spaceId;
    }

    /**
     * Adds a vehicle to the zone
     * @param vehicle is the vehicle added
     * @param spaceId the space it should be added to
     * @return whether it is successful
     */
    public boolean addVehicle(Vehicle vehicle, int spaceId) {
        boolean result = true;
        Space space = spaces[spaceId - 1];
        if (space.isAvailable()) {
            space.setOccupiedBy(vehicle);
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Removes vehicle from a space in the zone
     * @param space is where to remove the vehicle from
     */
    public void removeVehicle(int space) {
        spaces[space-1].setAvailable(true);
        spaces[space-1].setOccupiedBy(null);
        isFull = false;
    }

    private int findFreeSpace() {
        int foundSpaceId = 0;
        boolean found = false;
        for (Space s : spaces) {
            if (s.isAvailable()) {
                foundSpaceId = s.getId();
                s.setAvailable(false);
                found = true;
                break;
            }
        }
        if(!found) {
            isFull = true;
        }
        return foundSpaceId;
    }

    /**
     * Returns information about the Zone object as a String
     * @return a String of information
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Zone: ").append(id).append(", Occupied by: ");
        for (Space s: spaces) {
            result.append(s);
        }
        return result.toString();
    }
}
