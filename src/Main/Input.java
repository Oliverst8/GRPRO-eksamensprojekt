package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Input {
    private int size;
    private HashMap<String, Integer> objects;

    public Input(String filePath) {
        File file = new File(filePath);

        try {
            Scanner sc = new Scanner(file);

            size = Integer.parseInt(sc.nextLine());

            objects = new HashMap<String, Integer>();

            while (sc.hasNextLine()) {
                String object[] = sc.nextLine().split(" ");
                
                int amount;

                if (object[1].contains("-")) {
                    String[] range = object[1].split("-");
        
                    int low = Integer.parseInt(range[0]);
                    int high = Integer.parseInt(range[1]);
        
                    amount = (int) (Math.random() * (high - low)) + low;
                } else {
                    amount = Integer.parseInt(object[1]);
                }

                objects.put(object[0], amount);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }

    public int getSize() {
        return size;
    }

    public HashMap<String, Integer> getObjects() {
        return objects;
    }
}
