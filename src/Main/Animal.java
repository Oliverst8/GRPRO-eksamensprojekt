package Main;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.*;

public abstract class Animal extends Organism implements Consumable{

    private double hunger; //0 is empty, and 100 is full

    private final Class<? extends Consumable>[] canEat;

    protected boolean sleeping;

    /**
     * Initialises hunger to 50
     * Initialises food type to that the animal itself is to meat
     * Initialises the food that can be eaten
     */
    public Animal( Class<? extends Consumable>[] canEat, int defaultStrength) {
        super(defaultStrength);
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
    public boolean canIEat(Class<? extends Consumable> food) {
        for(Class<? extends Consumable> edibleFood : canEat) {
            if(food.equals(edibleFood)) return true;
        }
        return false;
    }

    /**
     * @param world the world the animal is in
     * @return The closest prey of the animal
     * @return null if there is no prey ind a location of 4
     */
    protected Organism findPrey(World world, int radius) {
        Map<Location, Organism> prey = new HashMap<>();
        for (Class<? extends Consumable> foodtype : canEat){
            Location preyLocation = findNearest(world, radius, foodtype);
            if(preyLocation == null) continue;
            Organism currentPrey;
            if(Helper.doesArrayContain(foodtype.getInterfaces(), NonBlocking.class)) currentPrey = (Organism) world.getNonBlocking(preyLocation);
            else currentPrey = (Organism) world.getTile(preyLocation);

            if(getFoodChainValue() >= currentPrey.getFoodChainValue()){
                prey.put(preyLocation, currentPrey);
            }
        }
        if(prey.isEmpty()) return null;
        Location closestPrey = null;
        double closestDist = Double.MAX_VALUE;
        for(Location currentPreyLocation : prey.keySet()){
            double dist = distance(world, currentPreyLocation);
            if(closestDist > dist){
                closestPrey = currentPreyLocation;
                closestDist = dist;
            }
        }
        return prey.get(closestPrey);
    }

    /**
     * Finds the closest prey the animal can hunt
     * If the distance to the cloest prey is 0, its get eaten
     * If the strengthweight of the prey is -1, it moves towards it, otherwise it checks if it can attak it
     * if it cant attack it, it moves towards it
     * @param world the world which the animal is on
     */
    protected void hunt(World world){
        huntPrey(world,findPrey(world, 4));
    }


    protected void huntPrey(World world, Organism prey){
        if(prey == null) return;
        Location preyLocation = world.getLocation(prey);
        double distanteToPrey = distance(world, preyLocation);
        if(prey.getFoodChainValue() == -1){
            if(distanteToPrey == 0){
                eat(prey, world);
            } else {
                moveTowards(preyLocation, world);
            }
        } else{
            if(distanteToPrey < 2){
                Attack(world, prey);
            } else{
                moveTowards(preyLocation, world);
            }
        }
    }

    private void Attack(World world, Organism animal) {
        throw new UnsupportedOperationException("Attack method not implemented yet");
    }

    /**
     * @throws NullPointerException if food is null
     * Check weather or not the animal can eat the food
     * Adds energy if it can, and does nothing if not.
     * @param food the food to be eaten
     */
    public void eat(Organism food, World world) {
        if(canIEat(food.getClass())){
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
    protected Location findNearest(World world, int radius, Class<?> object) {
        if(radius < 2) throw new IllegalArgumentException("Radius cant be less then 2");

        Location nearestObject = null;
        double smallestDistance = Integer.MAX_VALUE;

        Set<Location> surrondingTiles = world.getSurroundingTiles(radius); //Document tation says empty tiles but code says it dosent matter
        Object standingOnObject;

        if(world.containsNonBlocking(world.getCurrentLocation())) standingOnObject = world.getNonBlocking(world.getCurrentLocation()); //Gets the nonblocking object the animal is standing on if its standing on one

        else standingOnObject = null;

        if(standingOnObject != null) surrondingTiles.add(world.getLocation(standingOnObject)); //If the animal is standing on an object, adds it location to the set

        for(Location tile : surrondingTiles) {
            if(world.getTile(tile) == null) continue;

            Class<?> tileObject;

            if(Helper.doesArrayContain(object.getInterfaces(), NonBlocking.class)) {
                if(world.containsNonBlocking(tile)) {
                    tileObject = world.getNonBlocking(tile).getClass();
                } else {
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
     * @throws IllegalArgumentException if the animal is already on the position;
     * Returns without doing anything if the object is already standing on the location
     * Moves towards location by one tile
     * Remove 10 energy
     */
    protected void moveTowards(Location location, World world) {
        moveTowards(location, world, 1, this);
    }

    /**
     * @throws IllegalArgumentException if the animal is already on the position;
     * Returns without doing anything if the object is already standing on the location
     * Moves towards location by amount of steps tiles
     * Remove 10 energy
     */
    protected void moveTowards(Location location, World world, int amountOfSteps, Animal animal) {
        Location newTile = world.getLocation(animal);
        for(int i = 0; i < amountOfSteps; i++) {
            if (newTile.getX() == location.getX() && newTile.getY() == location.getY())
                throw new IllegalArgumentException("Animal is already there");

            int x = makeNumberOneCloser(newTile.getX(), location.getX());
            int y = makeNumberOneCloser(newTile.getY(), location.getY());

            if (!world.isTileEmpty(new Location(x, y))) {
                if (world.isTileEmpty(new Location(x, newTile.getY()))) y = newTile.getY();
                else if (world.isTileEmpty(new Location(newTile.getX(), y))) x = newTile.getX();
                else break;
            }
            newTile = new Location(x,y);


        }
        System.out.println(this + " moves from: " + world.getLocation(this) + " to: " + newTile);
        world.move(this, newTile);
        removeEnergy(10*amountOfSteps);
    }



    protected void moveAwayFrom(World world, Location location){
        int x = makeNumberOneFurtherAway(world.getCurrentLocation().getX(), location.getX());
        int y = makeNumberOneFurtherAway(world.getCurrentLocation().getY(), location.getY());
        x = validateCordinate(world, x);
        y = validateCordinate(world, y);
        world.move(this, new Location(x,y));
    }

    protected int validateCordinate(World world, int cordinate){
     int returnNumber = Math.min(world.getSize(), cordinate);
     returnNumber = Math.max(0, returnNumber);
     return returnNumber;
    }

    protected  int makeNumberOneFurtherAway(int actual, int target){
        if(actual < target) return (actual - 1);
        else return (actual + 1);

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
    protected void sleep() {
        if(hunger > 10) {
            removeHunger(10);
            addEnergy(10);
        }
    }

    public double getHunger() {
        return hunger;
    }

    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    /*
     * Adds hunger
     * @param hunger gets added to current hunger
     */
    public void addHunger(double hunger){
        this.hunger = Math.max(100, this.hunger + hunger);
    }

    /*
     * Removes hunger
     * @param hunger get subtracted from current hunger
     */
    public void removeHunger(double hunger){
        this.hunger = Math.min(0, this.hunger + hunger);
    }

    /*
     * @return String array of canEat of the object
     */
    public Class<? extends Consumable>[] getCanEat(){
        return canEat;
    }
}
