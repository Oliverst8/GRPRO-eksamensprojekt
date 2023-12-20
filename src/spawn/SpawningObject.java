package spawn;

import itumulator.world.Location;

public class SpawningObject {
    private final String className;
    private final int amount;
    private Location location;
    private final boolean infected;

    /**
     * @param className the type of the object
     * @param amount the amount of the object
     */
    public SpawningObject(String className, int amount, boolean infected) {
        this.className = className;
        this.amount = amount;
        this.infected = infected;
    }

    /**
     * @param className the className of the object
     * @param amount the amount of the object
     * @param location the location of the object
     */
    public SpawningObject(String className, int amount, boolean infected, Location location) {
        this.className = className;
        this.amount = amount;
        this.location = location;
        this.infected = infected;
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

    /**
     * @return whether the object is infected or not
     */
    public boolean isInfected() {
        return infected;
    }
}
