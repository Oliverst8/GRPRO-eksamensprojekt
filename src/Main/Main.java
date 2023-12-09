package Main;

import java.util.ArrayList;

import spawn.Input;
import spawn.ObjectFactory;
import spawn.SpawningObject;

import itumulator.world.World;
import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        Input input = new Input("data/week3/t3-2ab.txt");
        
        int delay = 250;
        int display_size = 1000;
        int size = input.getSize();
        
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();

        generateObjects(world, input.getObjects());

        program.getCanvas().setIsomorphic(true);
        program.show();

        while(true) program.simulate();
    }

    private static void generateObjects(World world, ArrayList<SpawningObject> objects) {
        for (SpawningObject object : objects) {
            for (int i = 0; i < object.getAmount(); i++) {
                Object newObject = null;

                // Generate object
                if(object.getLocation() != null) {
                    newObject = ObjectFactory.generateOnMap(world, object.getLocation(), object.getClassName(), object.getLocation());
                } else {
                    newObject = ObjectFactory.generateOnMap(world, object.getClassName());
                }

                // Set infected
                if (object.isInfected()) {
                    if (newObject instanceof Carcass) {
                        ((Carcass) newObject).setInfected(new Ghoul());
                    } else {
                        ((Animal) newObject).setInfected(new Cordyceps());
                    }
                }
            }
        }
    }
}