package test;

import Main.Berry;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BerryTest {
    Program program;
    World world;

    @BeforeEach
    void setup() {
        int size = 2; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }

    @Test
    void testIfBushLosesItsBerriesAndStaysOnMap() {
        Berry berry = (Berry) ObjectFactory.generateOnMap(world, "Berry");
        assertTrue(berry.containsBerries());
        berry.die(world);
        assertFalse(berry.containsBerries());
        assertTrue(world.contains(berry));
    }

    @Test
    void testIfBerryBushDissapearsWhenDying() {
        Berry berry = (Berry) ObjectFactory.generateOnMap(world, "Berry");
        berry.die(world);
        berry.die(world);
        assertFalse(world.contains(berry));
    }

    @Test
    void testIfBushGrowsBerriesAfter2Days() {
        Berry berry = (Berry) ObjectFactory.generateOnMap(world, "Berry");
        berry.die(world);
        for(int i = 0; i < 2*19; i++){
            program.simulate();
            assertFalse(berry.containsBerries());
        }
        program.simulate();
        program.simulate();
        assertTrue(berry.containsBerries());
    }
}
