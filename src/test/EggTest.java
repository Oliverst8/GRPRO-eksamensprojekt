package test;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import main.Egg;

import main.Turtle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spawn.ObjectFactory;

import static org.junit.jupiter.api.Assertions.*;

public class EggTest {

    Program program;
    World world;
    Egg egg;

    @BeforeEach
    void setUp(){
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
        egg = (Egg) ObjectFactory.generateOnMap(world, new Location(0,0),"Egg", Turtle.class);
    }

    @Test
    void testIfEggHatches(){
        for(int i = 0; i < 20; i++){
            assertTrue(world.contains(egg));
            assertEquals(1, world.getEntities().size());
            egg.act(world);
        }
        assertFalse(world.contains(egg));
        assertEquals(1, world.getEntities().size());
    }

    @Test
    void testHatchSpecies(){
        Location spawningLocation = world.getLocation(egg);
        for(int i = 0; i < 20; i++){
            assertTrue(world.contains(egg));
            assertEquals(1, world.getEntities().size());
            egg.act(world);
        }
        assertInstanceOf(Turtle.class, world.getTile(spawningLocation));
    }

}
