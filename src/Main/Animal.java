package Main;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.Set;

public abstract class Animal extends Organism {

    private double hunger; //0 is empty, and 100 is full

    private final String[] canEat;

    protected boolean sleeping;

    /**
     * Initialises hunger to 50
     * Initialises food type to that the animal itself is to meat
     * Initialises the food that can be eaten
     */
    public Animal( String[] canEat) {
        super("meat");
        hunger = 50;
        this.canEat = canEat;
        sleeping = false;
    }

    /**
     * Checks if the object that the function is called from can eat a type of food
     * Returns false if not
     * @param food String with food
     * @return true if String food is inside of canEat of the animal
     */
    private boolean canIEat(String food){
        for(String edibleFood : canEat){
            if(food.equals(edibleFood)) return true;
        }
        return false;
    }

    /**
     * @throws NullPointerException if food is null
     * Check weather or not the animal can eat the food
     * Adds energy if it can, and does nothing if not.
     * @param food the food to be eaten
     */
    protected void eat(Organism food, World world) {
        if(food == null) throw new NullPointerException("Food cant be null");
        if(canIEat(food.getFoodType())){
            addHunger(0.5 * food.getEnergy());
            food.die(world);
        }
    }

    /**
     * @throws IllegalArgumentException if radius is less then 2
     * @throws NullPointerException if the world or object is null
     * Finds the nearest object of the type object to this animal
     * @return the location of the nearest object (except itself) in radius, returns null if there is no such object
     */
    protected Location findNearest(World world, int radius, Class<?> object){

        if(radius < 2) throw new IllegalArgumentException("Radius cant be less then 2");
        if(world == null || object == null) throw new NullPointerException("Arguments cant be null");

        Location nearestObject = null;
        double smallestDistance = Integer.MAX_VALUE;

        Set<Location> surrondingTiles = world.getSurroundingTiles(radius); //Document tation says empty tiles but code says it dosent matter
        Object standingOnObject;

        if(world.containsNonBlocking(world.getCurrentLocation())) standingOnObject = world.getNonBlocking(world.getCurrentLocation()); //Gets the nonblocking object the animal is standing on if its standing on one

        else standingOnObject = null;

        if(standingOnObject != null) surrondingTiles.add(world.getLocation(standingOnObject)); //If the animal is standing on an object, adds it location to the set

        for (Location tile : surrondingTiles) {
            if(world.getTile(tile) == null) continue;

            Class<?> tileObject;

            if(Helper.doesArrayContain(object.getInterfaces(), NonBlocking.class)) {
                try{
                    tileObject = world.getNonBlocking(tile).getClass();
                } catch (IllegalArgumentException e){
                    continue;
                }
            } else tileObject = world.getTile(tile).getClass();

            if(tileObject.equals(object)){ //Check if the tile is the same object as object in parameter

                double distance = distance(world, tile);

                if(distance < smallestDistance) {
                    nearestObject = tile;
                    smallestDistance = distance;
                }
            }
        }
        return nearestObject;
    }

    /**
     * Finds the distance between two objects
     * @param world the world the animal is in
     * @param location2 the location of the second object
     * @return the distance between two object
     */
    protected double distance(World world, Location location2) {
        return Math.sqrt(Math.pow(world.getCurrentLocation().getX() - location2.getX(), 2) + Math.pow(world.getCurrentLocation().getY() - location2.getY(), 2));
    }

    abstract void produceOffSpring(World world);
    protected void reproduce(World world, Animal animal1, Animal animal2) throws cantReproduceException {
        if (animal1.getAge() < animal1.getAdultAge()) throw new cantReproduceException(animal1, animal2);
        if (animal2.getAge() < animal2.getAdultAge()) throw new cantReproduceException(animal1, animal2);
        if (!animal1.getClass().equals(animal2.getClass())) throw new cantReproduceException(animal1, animal2);
        if (!(animal1.getEnergy() > 50 && animal2.getEnergy() > 50)) throw new cantReproduceException(animal1, animal2);
        animal1.removeEnergy(50);
        animal2.removeEnergy(50);
        produceOffSpring(world);
    }

    /**
     * Throws IllegalArgumentException if world or location is null
     * Returns without doing anything if the object is already standing on the location
     * Moves towards location by one tile
     * Remove 10 energy
     */
    protected void moveTowards(Location location, World world){
        if(world == null) throw new NullPointerException("World argument cant be null");
        if(location == null) throw new NullPointerException("Location argument cant be null");
        if(world.getCurrentLocation().getX() == location.getX() && world.getCurrentLocation().getY() == location.getY()) throw new IllegalArgumentException("Animal is already there");
        int x = makeNumberOneCloser(world.getCurrentLocation().getX(), location.getX());
        int y = makeNumberOneCloser(world.getCurrentLocation().getY(), location.getY());
        if(!world.isTileEmpty(new Location(x,y))){
        return;
        }
        world.move(this, new Location(x,y));
        removeEnergy(10);
    }

    /**
     * @param actual the current number
     * @param target the number you want actual to be closer to
     * @return the closer number
     */
    protected int makeNumberOneCloser(int actual, int target) {
        if(actual < target) return (actual + 1);
        else if (actual > target) return (actual - 1);
        else return actual;
    }

    /*
     * If energy is less than 100
     * - Removes 10 hunger and adds 10 energy
     * Set sleeping to true
     */
    protected void sleep(){
        if(hunger > 10){
            removeHunger(10);

            addEnergy(10);
        }

    }

    public double getHunger() {
        return hunger;
    }

    public void setHunger(double hunger){
        this.hunger = hunger;
    }

    /*
     * Adds hunger
     * @param hunger gets added to current hunger
     * returns the smallest number between 100 and this.hunger + @param hunger
     */
    public void addHunger(double hunger){
        this.hunger = Math.max(100, this.hunger + hunger);
    }

    /*
     * Removes hunger
     * @param hunger get subtracted from current hunger
     * returns the biggest number between 0 and this.hunger + hunger
     */
    public void removeHunger(double hunger){
        this.hunger = Math.min(0, this.hunger + hunger);
    }

    /*
     * @return String array of canEat of the object
     */
    public String[] getCanEat(){
        return canEat;
    }
}
