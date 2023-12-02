package Main;

import java.awt.Color;
import java.util.Random;

import itumulator.world.World;
import itumulator.world.Location;

public class Bear extends Animal {
    Location territory;
    int territoryRadius = 3;

    public Bear(World world) {
        super(new Class[]{Wolf.class}, 3);

        adultAge = 5;

        Random random = new Random();

        Location location = new Location(random.nextInt(world.getSize()), random.nextInt(world.getSize()));

        this.territory = location;
    }

    public Bear(World world, Location territory) {
        super(new Class[]{Wolf.class}, 3);

        adultAge = 5;

        this.territory = territory;
    }

    @Override
    void produceOffSpring(World world) {
        throw new UnsupportedOperationException("Unimplemented method 'produceOffSpring'");
    }

    @Override
    void dayBehavior(World world) {
        if(sleeping) {
            sleeping = false;
            grow();
        }

        if(age >= adultAge) {
            partrolTerritory(world);
        }
    }

    private void partrolTerritory(World world) {
        Random random = new Random();

        int lowerX = Math.max(0, territory.getX() - territoryRadius);
        int upperX = Math.min(world.getSize() - 1, territory.getX() + territoryRadius);
        int lowerY = Math.max(0, territory.getY() - territoryRadius);
        int upperY = Math.min(world.getSize() - 1, territory.getY() + territoryRadius);

        Location newLocation = new Location(random.nextInt(upperX - lowerX + 1) + lowerX, random.nextInt(upperY - lowerY + 1) + lowerY);

        moveTowards(world, newLocation);
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
    protected String getType() {
        return "bear";
    }

    @Override
    protected Color getColor() {
        return Color.lightGray;
    }
}
