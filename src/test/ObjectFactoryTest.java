package test;

import Main.Wolf;
import Main.Burrow;
import Main.Rabbit;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class ObjectFactoryTest {

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
    void testgenerateWithoutLocation() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, "rabbit");

        assertInstanceOf(Rabbit.class, rabbit);
    }

    @Test
    void testgenerateWithLocation() {
        Location location = new Location(1,1);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"rabbit");

        assertEquals(new Location(1,1),world.getLocation(rabbit));
    }

    @Test
    void generateWith3ConstructorArgumentsExpectsRabbit() {
        Burrow burrow = new Burrow(world, new Location(0,0));
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);

        assertInstanceOf(Rabbit.class, rabbit);
    }

    @Test
    void generateWith0ConstructorArgumentsExpectsRabbit() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit");

        assertInstanceOf(Rabbit.class, rabbit);
    }

    @Test
    void generateWith0ConstructorArgumentsExpectsWolf() {
        Wolf wolf = (Wolf) ObjectFactory.generateOffMap(world, "wolf");

        assertInstanceOf(Wolf.class, wolf);
    }

    @AfterEach
    void tearDown() {}
}