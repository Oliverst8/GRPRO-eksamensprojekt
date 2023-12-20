package main;

import java.util.ArrayList;

import spawn.Input;
import spawn.ObjectFactory;
import spawn.SpawningObject;

import itumulator.world.World;
import itumulator.executable.Program;

public class Main {
    /**
     * Generates objects from a given list of spawning objects.
     * @param world the world to generate objects in.
     * @param objects the list of spawning objects.
     */
    private static void generateObjects(World world, ArrayList<SpawningObject> objects) {
        for (SpawningObject object : objects) {
            Pack pack = null;

            // Create a pack if spawning object is a wolf
            if (object.getClassName().equals("Wolf")) pack = new Pack();

            // Iterate over amount of objects
            for (int i = 0; i < object.getAmount(); i++) {
                Object newObject = null;

                // Generate object
                if (object.getClassName().equals("Wolf")) {
                    newObject = ObjectFactory.generateOnMap(world, object.getClassName(), pack);
                } else if(object.getLocation() != null) {
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

    public static void main(String[] args) {
        Input input = new Input("data/demo/Video-demo-uge-4.txt");
        
        int delay = 75;
        int display_size = 800;
        int size = input.getSize();
        
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();

        generateObjects(world, input.getObjects());

        program.getCanvas().setIsomorphic(true);
        program.show();

        for (int i = 0; i < 200; i++) program.simulate();
    }
}