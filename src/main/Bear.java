package main;

import java.awt.Color;

import java.util.Random;

import spawn.ObjectFactory;
import error.CantReproduceException;

import itumulator.world.World;
import itumulator.world.Location;

public class Bear extends Animal {
    private final Location territory;

    private int matingDesire;
    private int territoryRadius;

    public Bear(World world) {
        super(3);

        initialize();

        Random random = new Random();

        Location location = new Location(random.nextInt(world.getSize()), random.nextInt(world.getSize()));

        territory = location;
    }

    public Bear(World world, Location territory) {
        super(3);

        initialize();

        this.territory = territory;
    }

    @Override
    public String getType() {
        return "bear";
    }

    @Override
    public Color getColor() {
        return Color.lightGray;
    }

    @Override
    protected void dayBehavior(World world) {
        if(isDying(world)) return;

        if(getHunger() <= 50) {
            territoryRadius = 8;
        } else {
            territoryRadius = 4;
        }

        if(isAdult()) {
            increaseMatingDesire(50);

            if(matingDesire >= 100) {
                boolean attemptingToMate = seekMate(world);

                if(attemptingToMate) return;
            }
        }

        partrolTerritory(world);
    }

    @Override
    protected void nightBehavior(World world) {
        Location currentLocation = world.getLocation(this);

        if(currentLocation.getX() >= territory.getX() + territoryRadius ||
                currentLocation.getX() <= territory.getX() - territoryRadius ||
                currentLocation.getY() >= territory.getY() + territoryRadius ||
                currentLocation.getY() <= territory.getY() - territoryRadius) {
            moveTowards(world, territory);
        } else {
            sleeping = true;
        }

        if(sleeping) {

            sleep();
            return;
        }
    }

    @Override
    protected void produceOffSpring(World world) {
        Location territoryLocation = Utility.findEmptyLocation(world);

        ObjectFactory.generateOnMap(world, territoryLocation, "Bear");
    }


    @Override
    protected void setupCanEat() {
        addCanEat(Berry.class);
        addCanEat(Carcass.class);
        addCanEat(Wolf.class);
        addCanEat(Rabbit.class);
        addCanEat(Bear.class);
        addCanEat(Turtle.class);
    }

    public Location  getTerritory(){
        return  territory;
    }

    /**
     * Initializes the variables of the bear
     */
    private void initialize() {
        adultAge = 1;

        damage = 125;

        maxEnergy = 300;
        maxHealth = 350;

        energy = maxEnergy;
        health = maxHealth;
        territoryRadius = 4;
    }

    /**
     * If the bear finds prey in its territory, it will hunt it.
     * Otherwise, it will move to a random location in its territory.
     * @param world the world the bear is in.
     */
    private void partrolTerritory(World world) {
        Random random = new Random();

        // Get the bounds of the territory.
        int lowerX = Math.max(0, territory.getX() - territoryRadius);
        int upperX = Math.min(world.getSize() - 1, territory.getX() + territoryRadius);
        int lowerY = Math.max(0, territory.getY() - territoryRadius);
        int upperY = Math.min(world.getSize() - 1, territory.getY() + territoryRadius);

        // If there is prey in the territory, hunt it.
        for(int x = lowerX; x <= upperX; x++) {
            for(int y = lowerY; y <= upperY; y++) {
                Location location = new Location(x, y);

                Entity object = (Entity) world.getTile(location);

                if(object != null && getCanEat().contains(object.getEntityClass()) &&
                ((Organism) object).isEatable()) {

                    //Skip if target is itself
                    if(object.equals(this)) continue;

                    // If the pray is an adult and this is not, do not hunt it.
                    if (!isAdult() && ((Organism) object).isAdult()) continue;

                    huntPrey(world, (Organism) object);
                    return;
                }
            }
        }

        // Otherwise, move to a random location in the territory.
        Location newLocation = new Location(random.nextInt(upperX - lowerX + 1) + lowerX, random.nextInt(upperY - lowerY + 1) + lowerY);

        moveTowards(world, newLocation);
    }

    /**
     * Increases the mating desire by the given amount.
     * @param amount the amount to increase the mating desire by.
     */
    private void increaseMatingDesire(int amount) {
        matingDesire = Math.min(matingDesire + amount, 100);
    }

    /**
     * Decreases the mating desire by the given amount.
     * @param amount the amount to decrease the mating desire by.
     */
    private void decreaseMatingDesire(int amount) {
        matingDesire = Math.max(matingDesire - amount, 0);
    }

    /**
     * Attempts to mate with the nearest mate.
     * @param world the world the bear is in.
     * @return true if the bear is attempting to mate, otherwise false.
     */
    private boolean seekMate(World world) {
        Object mate = findNearestPrey(world, 10, this.getClass());

        if(mate == null) return false;

        Location location = world.getLocation(mate);

        if(Utility.distance(world.getLocation(this), location) < 2) {
            try {
                reproduce(world, this, (Animal) world.getTile(location));
                decreaseMatingDesire(100);
            } catch(CantReproduceException e) {
                e.printInformation();
            }
        } else {
            moveTowards(world, location);
        }

        return true;
    }
}
