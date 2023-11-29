import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Input {
    private int size;
    private HashMap<String, String> Actors;

    public Input(String filePath) {
        File file = new File(filePath);

        try {
            Scanner sc = new Scanner(file);

            size = Integer.parseInt(sc.nextLine());

            Actors = new HashMap<String, String>();

            while (sc.hasNextLine()) {
                String object[] = sc.nextLine().split(" ");

                Actors.put(object[0], object[1]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }

    public int getSize() {
        return size;
    }

    public HashMap<String, String> getActors() {
        return Actors;
    }
}
