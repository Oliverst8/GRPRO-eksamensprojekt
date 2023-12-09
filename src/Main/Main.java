package Main;

import java.util.ArrayList;

import itumulator.world.World;
import itumulator.executable.Program;

import spawn.Input;
import spawn.ObjectFactory;
import spawn.SpawningObject;

public class Main {
    public static void main(String[] args) {
        Input input = new Input("data/week2/t2-5a.txt");
        
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
                if(object.getLocation() != null) {
                    ObjectFactory.generateOnMap(world, object.getLocation(), object.getClassName(), object.getLocation());
                } else {
                    ObjectFactory.generateOnMap(world, object.getClassName());
                }
            }
        }
    }
}