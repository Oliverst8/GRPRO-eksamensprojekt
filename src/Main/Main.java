package Main;

import java.util.ArrayList;

import itumulator.world.World;
import itumulator.executable.Program;

import spawn.Input;
import spawn.ObjectFactory;
import spawn.SpawningObject;

public class Main {

    public static void main(String[] args) {
        Input input = new Input("data/demo/d1.txt");
        
        int delay = 1000;
        int display_size = 1000;
        int size = input.getSize();
        
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();

        generateObjects(world, input.getObjects());

        program.show();
        while(true) program.simulate();
    }

    private static void generateObjects(World world, ArrayList<SpawningObject> objects) {
        for (SpawningObject object : objects) {
            if(object.getLocation() != null) {
                ObjectFactory.generateOnMap(world, object.getLocation(), object.getClassName());
            } else {
                ObjectFactory.generateOnMap(world, object.getClassName());
            }
        }
    }
}