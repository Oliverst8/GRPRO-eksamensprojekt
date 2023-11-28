package spawn;

import itumulator.world.Location;

public class SpawningObject {
    private String className;
    private int amount;
    private Location location;

    /**
     * @param type the type of the object
     * @param amount the amount of the object
     */
    public SpawningObject(String className, int amount) {
        this.className = className;
        this.amount = amount;
    }

    /**
     * @param className the className of the object
     * @param amount the amount of the object
     * @param location the location of the object
     */
    public SpawningObject(String className, int amount, Location location) {
        this.className = className;
        this.amount = amount;
        this.location = location;
    }

    /**
     * @return the className of the object
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return the amount of the object
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return the location of the object
     */
    public Location getLocation() {
        return location;
    }
}
