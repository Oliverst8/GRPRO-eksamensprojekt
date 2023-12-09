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
                boolean isInfected = false;

                String line = sc.nextLine();
                line = line.toLowerCase();

                if (line.equals("")) continue; // Skip if line is blank

                // Check if the object is infected
                if (line.contains("fungi") || line.contains("cordyceps")) {
                    isInfected = true;

                    line = line.replaceAll("fungi |cordyceps ", "");
                }

                String object[] = line.split(" ");

                object[0] = object[0].substring(0, 1).toUpperCase() + object[0].substring(1); // Capitalize the first letter

                if (object.length == 2) { // If the object is not given a location
                    objects.add(new SpawningObject(object[0], parseAmount(object[1]), isInfected));
                } else if (object.length == 3) { // If the object is given a location
                    objects.add(new SpawningObject(object[0], parseAmount(object[1]), isInfected, parseLocation(object[2])));
                }
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(0);
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
