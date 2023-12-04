package Main;

import java.awt.Color;

import java.util.Random;

import itumulator.world.World;
import itumulator.world.Location;

import spawn.ObjectFactory;

public class Bear extends Animal {
    Location territory;

    int matingDesire;
    int territoryRadius = 3;

    public Bear(World world) {
        super(3);

        adultAge = 5;

        Random random = new Random();

        Location location = new Location(random.nextInt(world.getSize()), random.nextInt(world.getSize()));

        this.territory = location;
    }

    public Bear(World world, Location territory) {
        super(3);

        adultAge = 5;

        this.territory = territory;
    }

    private void partrolTerritory(World world) {
        Random random = new Random();

        int lowerX = Math.max(0, territory.getX() - territoryRadius);
        int upperX = Math.min(world.getSize() - 1, territory.getX() + territoryRadius);
        int lowerY = Math.max(0, territory.getY() - territoryRadius);
        int upperY = Math.min(world.getSize() - 1, territory.getY() + territoryRadius);

        // If there is prey in the territory, hunt it.
        if(age >= adultAge) {
            for(int x = lowerX; x <= upperX; x++) {
                for(int y = lowerY; y <= upperY; y++) {
                    Location location = new Location(x, y);

                    Object object = world.getTile(location);

                    if(object != null && canEat.contains(object.getClass())) {
                        huntPrey(world, (Organism) object);
                        return;
                    }
                }
            }
        }

        // Otherwise, move to a random location in the territory.
        Location newLocation = new Location(random.nextInt(upperX - lowerX + 1) + lowerX, random.nextInt(upperY - lowerY + 1) + lowerY);

        moveTowards(world, newLocation);
    }

    private void increaseMatingDesire(int amount) {
        matingDesire = Math.min(matingDesire + amount, 100);
    }

    private void decreaseMatingDesire(int amount) {
        matingDesire = Math.max(matingDesire - amount, 0);
    }

    private void seekMate(World world) {
        Location location = findNearest(world, 10, this.getClass());

        if(location != null) {
            if(Helper.distance(world.getLocation(this), location) < 2) {
                try {
                    reproduce(world, this, (Animal) world.getTile(location));
                    decreaseMatingDesire(100);
                } catch(cantReproduceException e) {
                    e.printInformation();
                }
            } else {
                moveTowards(world, location);
            }
        }
    }

    @Override
    void produceOffSpring(World world) {
        Location territoryLocation = Helper.findEmptyLocation(world);

        ObjectFactory.generateOnMap(world, territoryLocation, getType());
    }

    @Override
    void dayBehavior(World world) {
        if(sleeping) {
            sleeping = false;
            grow();
        }

        if(age >= adultAge) {
            increaseMatingDesire(5);

            if(matingDesire >= 100) {
                seekMate(world);
                return;
            }
        }

        partrolTerritory(world);
    }

    @Override
    void nightBehavior(World world) {
        Location currentLocation = world.getLocation(this);

        if(currentLocation.getX() >= territory.getX() + territoryRadius ||
        currentLocation.getX() <= territory.getX() - territoryRadius ||
        currentLocation.getY() >= territory.getY() + territoryRadius ||
        currentLocation.getY() <= territory.getY() - territoryRadius) {
            moveTowards(world, territory);
        } else {
            sleep();
        }
    }

    @Override
    void setupCanEat() {
        canEat.add(Wolf.class);
    }

    @Override
    protected String getType() {
        return "bear";
    }

    @Override
    protected Color getColor() {
        return Color.lightGray;
    }
}
