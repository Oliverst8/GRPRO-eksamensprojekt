package Main;

import java.util.Set;
import java.util.Random;
import java.util.HashSet;

import itumulator.world.World;
import itumulator.world.Location;

public class Helper {
    private Helper() {}

    public static boolean doesArrayContain(Object[] array, Object value) {
        for(Object arrayValue : array) {
            if(arrayValue.equals(value)) return true;
        }

        return false;
    }

    /**
     *
     * @param world the world that needs to be locked in
     * @return and a location where there is no object that isnt nonBlocking
     */
    public static Location findEmptyLocation(World world) {
        return findNonFilledLocation(world, false);
    }

    public static boolean isThereAnEmptyLocationInWorld(World world, boolean nonBlockingNotAllowed) {
        Object[][][] worldTiles = world.getTiles();
        
        for (int i = 0; i < worldTiles.length; i++) {
            for (int j = 0; j < worldTiles[i].length; j++) {
                if(nonBlockingNotAllowed){
                    if(worldTiles[i][j][0] == null && worldTiles[i][j][1] == null) return true;
                } else if(worldTiles[i][j][1] == null) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * @param world the world that needs to be looked in
     * @param nonBlockingNotAllowed if true there cant be a non blocking object on the location either
     * @return a location where there is no object
     * @throws NoEmptyLocationException if there is no empty location
     */
    private static Location findNonFilledLocation(World world, boolean nonBlockingNotAllowed) {
        if(!isThereAnEmptyLocationInWorld(world, nonBlockingNotAllowed)) throw new NoEmptyLocationException();

        Random r = new Random();
        Location location;

        do{
            location = new Location(r.nextInt(world.getSize()),r.nextInt(world.getSize()));
        } while (!world.isTileEmpty(location) || (nonBlockingNotAllowed && world.containsNonBlocking(location)));

        return location;
    }

    /**
     *
     * @param world the world that needs to be locked in
     * @return a location where there is no object
     */
    public static Location findNonBlockingEmptyLocation(World world) {
        return findNonFilledLocation(world, true);
    }

    /**
     * Finds the distance between two objects
     * @param location1 the first location
     * @param location2 the location of the second object
     * @return the distance between two object
     */
    public static double distance(Location location1, Location location2) {
        return Math.sqrt(Math.pow(Math.abs(location1.getX() - location2.getX()), 2) + Math.pow(Math.abs(location1.getY() - location2.getY()), 2));
    }

    public static Set<Entity> getEntities(World world, Location location, int radius) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(location, radius);
        surroundingTiles.add(location);
        Set<Entity> entities = new HashSet<>();

        for(Location tile : surroundingTiles){
            Object tileObject = world.getTile(tile);
            if(tileObject != null){
                entities.add((Entity) tileObject);
                if(world.containsNonBlocking(tile)){
                    entities.add((Entity) world.getNonBlocking(tile));
                }
            }
        }

        return entities;
    }

    public static Entity findNearest(World world, Object searchingObject , int radius, Class<?> object) {
        if(radius < 2) throw new IllegalArgumentException("Radius cant be less then 2");

        Set<Entity> surroundingEntities = Helper.getEntities(world, world.getCurrentLocation(), radius);

        Entity nearestEntity = null;
        double smallestDistance = Double.MAX_VALUE;

        for(Entity entity : surroundingEntities){
            if(entity.equals(searchingObject)) continue;
            if(object.equals(entity.getClass())) continue;

            double distance = distance(world.getCurrentLocation(), world.getLocation(entity));
            if(distance < smallestDistance){
                smallestDistance = distance;
                nearestEntity = entity;
            }

        }

        return nearestEntity;
    }

    public static Set<Entity> filterByClass(Set<Entity> entities, Class<?> filterClass) {
        Set<Entity> filteredEntities = new HashSet<Entity>();

        for(Entity entity : entities){
            if(filterClass.isAssignableFrom(entity.getClass())){
                filteredEntities.add(entity);
            }
        }

        return filteredEntities;
    }

    public static Entity findNearest(World world, Location location ,Set<? extends Entity> entities) {

        Entity nearestEntity = null;
        double smallestDistance = Double.MAX_VALUE;

        for(Entity entity : entities){
            double distance = distance(location, world.getLocation(entity));
            if(distance < smallestDistance){
                smallestDistance = distance;
                nearestEntity = entity;
            }
        }

        return nearestEntity;
    }
}
