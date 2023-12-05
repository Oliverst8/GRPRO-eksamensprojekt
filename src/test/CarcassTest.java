package test;
import Main.Entity;
import Main.Rabbit;
import Main.Carcass;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
public class CarcassTest {
    Program program;
    World world;

    @BeforeEach
    void setUp() {
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }

    @Test
    void testIfCarcussSpawnsWhenAnimalDies() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "Rabbit");
        rabbit.setHealth(100);
        rabbit.removeHealth(100,world);
        rabbit.setEnergy(100);
        program.simulate();
        
        assertEquals(world.getTile(location).getClass(), Carcass.class);
    }

    @Test
    void testGetEntityClassOverride() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "Rabbit");
        rabbit.setEnergy(0);
        program.simulate();

        assertTrue(((Entity) world.getTile(location)).getEntityClass().equals(rabbit.getEntityClass()));
    }

    @Test
    void testCarcassDissapersAfter20Nights() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "Rabbit");
        rabbit.setHealth(100);
        rabbit.removeHealth(100,world);
        program.simulate();

        for(int i = 0; i <= 100; i++){
            program.simulate();
        }

        assertNull(world.getTile(location));
    }
}
