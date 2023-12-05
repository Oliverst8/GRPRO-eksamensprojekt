package spawn;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.ArrayList;

import itumulator.world.Location;

public class Input {
    private int size;
    private ArrayList<SpawningObject> objects;

    /**
     * @param filePath the path to the file that contains the input
     */
    public Input(String filePath) {
        File file = new File(filePath);

        try {
            Scanner sc = new Scanner(file);

            size = Integer.parseInt(sc.nextLine());

            objects = new ArrayList<SpawningObject>();

            while (sc.hasNextLine()) {
                String object[] = sc.nextLine().split(" ");

                if (object.length == 0) continue; // Skip if line is blank

                if (object.length == 2) { // If the object is not given a location
                    objects.add(new SpawningObject(object[0], parseAmount(object[1])));
                } else if (object.length == 3) { // If the object is given a location
                    objects.add(new SpawningObject(object[0], parseAmount(object[1]), parseLocation(object[2])));
                }
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private int parseAmount(String amount) {
        if (amount.contains("-")) { // If the amount is a range
            String[] range = amount.split("-");

            int low = Integer.parseInt(range[0]);
            int high = Integer.parseInt(range[1]);

            return (int) (Math.random() * (high - low)) + low;
        }
        
        return Integer.parseInt(amount);
    }

    private Location parseLocation(String location) {
        String[] coordinates = location.substring(1, location.length() - 1).split(",");

        return new Location(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
    }

    /**
     * @return the size of the world
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the objects and how many of them there should be
     */
    public ArrayList<SpawningObject> getObjects() {
        return objects;
    }
}
