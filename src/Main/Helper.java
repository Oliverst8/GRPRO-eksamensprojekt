package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;

public class Helper {
    private Helper(){}

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
    private static Location findNonFilledLocation(World world, boolean nonBlockingNotAllowed){
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
    public static Location findNonBlockingEmptyLocation(World world){
        return findNonFilledLocation(world, true);
    }
}
