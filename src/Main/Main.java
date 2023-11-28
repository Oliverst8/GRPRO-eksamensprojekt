package Main;

import itumulator.executable.Program;
import itumulator.world.World;
import spawn.Input;
import spawn.ObjectFactory;
import spawn.SpawningObject;

public class Main {

    public static void main(String[] args) {

        Input input = new Input("data/week1/tf1-1.txt");

        int size = input.getSize(); // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1000; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)

        Program program = new Program(size, display_size, delay); // opret et nyt program
        World world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!

        // Generates the objects in the world
        for (SpawningObject object : input.getObjects()) {
            if(object.getLocation() != null) {
                ObjectFactory.generate(world, object.getLocation(), object.getClassName());
                continue;
            } else {
                ObjectFactory.generate(world, object.getClassName());
            }
        }

        program.show(); // viser selve simulationen
        for (int i = 0; i < 200; i++) {
            program.simulate();
        } // kører 200 runder, altså kaldes 'act' 200 gange for alle placerede aktører
    }
}