package test;

import Main.Wolf;
import Main.Burrow;
import Main.Rabbit;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
        assertInstanceOf(Rabbit.class, ObjectFactory.generateOnMap(world, "Rabbit"));
    }

    @Test
    void testgenerateWithLocation() {
        Location location = new Location(1,1);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");
        
        assertEquals(new Location(1,1),world.getLocation(rabbit));
    }

    @Test
    void generateWith3ConstructorArgumentsExpectsRabbit() {
        Burrow burrow = new Burrow(world, new Location(0,0));
        assertInstanceOf(Rabbit.class, ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true));
    }

    @Test
    void generateWith0ConstructorArgumentsExpectsRabbit(){
        assertInstanceOf(Rabbit.class, ObjectFactory.generateOffMap(world, "Rabbit") );
    }

    @Test
    void generateWith0ConstructorArgumentsExpectsWolf(){
        assertInstanceOf(Wolf.class, ObjectFactory.generateOffMap(world, "Wolf") );
    }
}