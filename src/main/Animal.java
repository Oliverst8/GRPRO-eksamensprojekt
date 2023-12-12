package main;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

import spawn.ObjectFactory;
import error.CantReproduceException;

import itumulator.world.World;
import itumulator.world.Location;

public abstract class Animal extends MycoHost implements Spawnable {
    protected boolean sleeping;
    protected int strength;
    private double hunger; //0 is empty, and 100 is full
    private Set<Class<? extends Consumable>> canEat; //Holdes the types of classes the animal can eat

    /**
     * Initialises hunger to 50
     * Initialises food type to that the animal itself is to meat
     * Initialises the food that can be eaten
     */
    public Animal(int defualtFoodChainValue) {
        super(defualtFoodChainValue);
        hunger = 50;
        this.setCanEat(new HashSet<>());
        sleeping = false;
        setupCanEat();
    }

    protected abstract void setupCanEat();
    protected abstract void produceOffSpring(World world);

    /**
     * Calls a check of weather or not the animal is dying
     * @param world the world which the animal is in
     */
    @Override
    protected void dayBehavior(World world) {
        isDying(world);
    }

    @Override
    public String getPNGPath() {
        StringBuilder path = new StringBuilder();

        path.append(getType());

        if(isAdult()) path.append("-large");
        else path.append("-small");

        if (isInfected()) path.append("-fungi");
        if (sleeping) path.append("-sleeping");

        return path.toString();
    }

    /**
     * If the animal is in the world, it creates a carcuss of the animal in the animals location, and deletes the animal
     * otherwise it just deletes the animal
     * @param world current world
     */
    @Override
    public void die(World world){

        if(isInfected()){
            super.die(world);
            return;
        }
        if(world.isOnTile(this)) {
            Location carcassLocation = world.getLocation(this);
            world.delete(this);

            Carcass carcass = (Carcass) ObjectFactory.generateOnMap(world, carcassLocation, "Carcass");
            carcass.setAnimal(this);

        } else {
            super.die(world);
        }
    }

    /**
     * @throws NullPointerException if food is null
     * Check weather or not the animal can eat the food
     * Adds energy if it can, and does nothing if not.
     * @param food the food to be eaten
     */
    public void eat(World world, Organism food) {
        if(canIEat(food.getEntityClass())){
            if(food.getEnergy()>0){
                addHunger(0.5*food.getEnergy());
                food.removeEnergy(food.getEnergy());
            }
            food.die(world);
        }
    }

    public int getStrength() {
        return strength;
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
        setHunger(Math.max(100, this.hunger + hunger));
    }

    /*
     * Removes hunger
     * @param hunger get subtracted from current hunger
     */
    public void removeHunger(double hunger){
        setHunger(Math.max(0, this.hunger - hunger));
    }

    /*
     * @return String array of canEat of the object
     */
    public Set<Class<? extends Consumable>> getCanEat(){
        return canEat;
    }

    /**
     * Checks if the object that the function is called from can eat a type of food
     * Returns false if not
     * @param food The type of class that is trying to get eaten
     * @return true if the class is in the list of classes the animal can eat
     */
    public boolean canIEat(Class<? extends Consumable> food) {
        return getCanEat().contains(food);
    }

    /**
     * @param world the world the animal is in
     * @param radius the raidus to be looked for prey in
     * @return The closest prey of the animal
     * @return null if there is no prey ind a location of radius
     * An organism is only considered prey if its foodchainvalue is lower then the animal hunting for it, and the animal its that type of organism
     */
    protected Organism findPrey(World world, int radius) {
        Map<Location, Organism> prey = new HashMap<>();

        for(Entity entity : Helper.getEntities(world, world.getLocation(this),radius)) {
            if(getCanEat().contains(entity.getEntityClass())) {
                Organism currentPrey = (Organism) entity;

                if(getFoodChainValue() >= currentPrey.getFoodChainValue() && currentPrey.isEatable()) {
                    prey.put(world.getLocation(entity), currentPrey);
                }
            }
        }

        if(prey.isEmpty()) return null;
        
        Location closestPrey = null;

        double closestDist = Double.MAX_VALUE;
        for(Location currentPreyLocation : prey.keySet()){
            double dist = Helper.distance(world.getLocation(this), currentPreyLocation);
            if(closestDist > dist){
                closestPrey = currentPreyLocation;
                closestDist = dist;
            }
        }
        
        return prey.get(closestPrey);
    }

    /**
     Hunts the closet prey to the animal
     * @param world the world which the animal is on
     */
    protected void hunt(World world){
        huntPrey(world,findPrey(world, 4));
    }

    /**
     * Check weather or not the prey is something to be fought, or can already be eaten
     * If the prey has a foodchainvalue of -1 that means that the animal needs to stand on it to eat it
     * - If this is the case, checks if the animal is already on it, if it is it eats it, if not it moves towards it
     * If the prey has a foodchainvalue of -2 that means that the animal needs to stand next to it to eat it
     * - if this is the case, it checks if it stands next to it, if it does it eats it, if not it moves towards it
     * Otherwise it check if it is close enough to attack
     * - If it is it does
     * - otherwise it moves towards the prey
     * @param world the world the organisms are in
     * @param prey the prey to be hunted nothing happens if null
     */
    protected void huntPrey(World world, Organism prey){
        if(prey == null) return;
        Location preyLocation = world.getLocation(prey);
        double distanteToPrey = Helper.distance(world.getLocation(this),preyLocation);
        if(prey.getFoodChainValue() == -1) {
            if(distanteToPrey == 0){
                eat(world, prey);
            } else {
                moveTowards(world, preyLocation);
            }
        } else if(prey.getFoodChainValue() == -2) {
            if(distanteToPrey < 2) {
                eat(world, prey);
            } else {
                moveTowards(world, preyLocation);
            }
        }else{
            if(distanteToPrey < 2){
                Attack(world, prey);
            } else{
                moveTowards(world, preyLocation);
            }
        }
    }

    /**
     * @throws IllegalArgumentException if radius is less then 2
     * Finds the nearest object of the type object to this animal
     * @return the location of the nearest object (except itself) in radius, returns null if there is no such object
     */
    protected Entity findNearestPrey(World world, int radius, Class<?> object) {

        if(radius < 2) throw new IllegalArgumentException("Radius cant be less then 2");

        Set<Entity> surroundingEntities = Helper.getEntities(world, world.getCurrentLocation(), radius);

        Entity nearestEntity = null;
        double smallestDistance = Double.MAX_VALUE;

        for(Entity entity : surroundingEntities){
            if(entity.equals(this)) continue;
            if(!entity.getEntityClass().equals(object)) continue;

            double distance = Helper.distance(world.getLocation(this), world.getLocation(entity));
            if(distance < smallestDistance){
                smallestDistance = distance;
                nearestEntity = entity;
            }
        }

        return nearestEntity;
    }

    /**
     * @param world the world of the animals
     * @param animal1 the first animal
     * @param animal2 the second animal
     * @throws CantReproduceException if the animals arent old if enough to breed
     * @throws CantReproduceException if the animals arent the same type of animals
     * @throws CantReproduceException if the animals dont have enough energy
     */
    protected void reproduce(World world, Animal animal1, Animal animal2) throws CantReproduceException {
        if (animal1.getAge() < animal1.getAdultAge()) throw new CantReproduceException(animal1, animal2);
        if (animal2.getAge() < animal2.getAdultAge()) throw new CantReproduceException(animal1, animal2);
        if (!animal1.getEntityClass().equals(animal2.getEntityClass())) throw new CantReproduceException(animal1, animal2);
        if (!(animal1.getEnergy() > 50 && animal2.getEnergy() > 50)) throw new CantReproduceException(animal1, animal2);
        animal1.removeEnergy(50);
        animal2.removeEnergy(50);
        animal2.skipTurn();
        produceOffSpring(world);
    }

    /**
     * @throws IllegalArgumentException if the animal is already on the position;
     * Returns without doing anything if the object is already standing on the location
     * Moves towards location by one tile
     * Remove 10 energy
     */
    protected void moveTowards(World world, Location location) {
        moveTowards(world, location, 1, this);
    }

    /**
     * @throws IllegalArgumentException if the animal is already on the position;
     * Returns without doing anything if the object is already standing on the location
     * Moves towards location by amount of steps tiles
     * Remove 10 energy
     */
    protected void moveTowards(World world, Location location, int amountOfSteps, Animal animal) {
        Location newTile = world.getLocation(animal);

        for(int i = 0; i < amountOfSteps; i++) {
            if (newTile.getX() == location.getX() && newTile.getY() == location.getY()) {
                System.out.println("Animal is already on the location");
                return;
            }

            int x = makeNumberOneCloser(newTile.getX(), location.getX());
            int y = makeNumberOneCloser(newTile.getY(), location.getY());

            if (!world.isTileEmpty(new Location(x, y))) {
                if (world.isTileEmpty(new Location(x, newTile.getY()))) y = newTile.getY();
                else if (world.isTileEmpty(new Location(newTile.getX(), y))) x = newTile.getX();
                else break;
            }

            newTile = new Location(x,y);
        }

        world.move(this, newTile);
        world.setCurrentLocation(newTile);
        removeEnergy(2 * amountOfSteps);
    }

    /**
     * Moves the animal on block away from a location
     * @param world the world of the animal
     * @param location the location to move away from
     */
    protected void moveAwayFrom(World world, Location location) {
        int x = makeNumberOneFurtherAway(world.getCurrentLocation().getX(), location.getX());
        int y = makeNumberOneFurtherAway(world.getCurrentLocation().getY(), location.getY());
        x = validateCordinate(world, x);
        y = validateCordinate(world, y);

        world.move(this, new Location(x,y));
    }

    /**
     * Makes it so a single coordinate is inside the world
     * @param world the world the animal is in
     * @param coordinate the coordinate to be checked
     * @return the new coordinate that is in the world
     */
    protected int validateCordinate(World world, int coordinate){
     int returnNumber = Math.min(world.getSize()-1, coordinate);
     returnNumber = Math.max(0, returnNumber);
     return returnNumber;
    }

    /**
     * Makes a number one further away from another
     * @param actual the current number
     * @param target the target to get furhter away from
     * @return the new number
     */
    protected int makeNumberOneFurtherAway(int actual, int target){
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
        addHealth(2);
        sleeping = true;
        if(hunger >= 10) {
            removeHunger(10);
            addEnergy(10);
        } else if(hunger > 0) {
            removeHunger(hunger);
            addEnergy((int)hunger);
        }

    }

    protected void wake() {
        sleeping = false;
    }

    protected void addCanEat(Class<? extends Consumable> food){
        canEat.add(food);
    }

    protected void setCanEat(Set<Class<? extends Consumable>> canEat) {
        this.canEat = canEat;
    }

    /**
     * Checks weather or not the animal should die or if it should go to sleep because of energi needs
     * @param world The world the animal is in
     * @return weather the animal is dying or having to sleep
     */
    protected boolean isDying(World world){
        if(sleeping && hunger >= 0 && world.getCurrentTime() > 0) {
            sleep();
            return true;
        } else if(energy<=0 && health > 0 && hunger > 1) {
            sleep();
            return true;
        } else if(world.getCurrentTime() == 0 && sleeping) {
            sleeping = false;
            wake();
            return true;
        } else  if(health <= 0 || energy <= 0){
            die(world);
            return true;
        }
        return false;
    }

    /**
     * removes 10 energy from the prey
     * if it hits 0 energy die will be called from organism.java
     * @param world
     * @param animal
     */
    private void Attack(World world, Organism animal) {
        animal.removeHealth(strength, world);
        this.removeEnergy(10);
    }
}