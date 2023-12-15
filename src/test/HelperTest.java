package test;

import java.util.HashSet;
import java.util.Set;

import main.Wolf;
import main.Animal;
import main.Entity;
import main.Rabbit;
import main.Helper;
import main.Carcass;
import main.Organism;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {
    Program program;
    World world;

    @BeforeEach
    void setUp() {
        int size = 4; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }

    @Test
    void testDoesArrayContainItDoesMultipleElements() {
        assertTrue(Helper.doesArrayContain(new Object[]{"27",3},"27"));
    }

    @Test
    void testDoesArrayContainItDoesOneElements() {
        assertTrue(Helper.doesArrayContain(new Object[]{"27"},"27"));
    }

    @Test
    void testDoesArrayContainItDoesNotMultipleElements() {
        assertFalse(Helper.doesArrayContain(new Object[]{"27",3},"28"));
    }

    @Test
    void testDoesArrayContainItDoesNooneElements() {
        assertFalse(Helper.doesArrayContain(new Object[]{"27"},"28"));
    }

    @Test
    void testDoesArrayContainItDoesNoElements() {
        assertFalse(Helper.doesArrayContain(new Object[]{},"28"));
    }

    @Test
    void isThereAnEmptyLocationInWorldAllowNonBlockingWtihNoObjectsInWorldExpectsTrue() {
        assertTrue(Helper.isThereAnEmptyLocationInWorld(world, false));
    }

    @Test
    void isThereAnEmptyLocationInWorldAllowNonBlockingWtih1ObjectsInWorldExpectsTrue() {

        ObjectFactory.generateOnMap(world, "Grass");

        assertTrue(Helper.isThereAnEmptyLocationInWorld(world, false));
    }

    @Test
    void isThereAnEmptyLocationInWorldDontAllowNonBlockingWtih1ObjectInWorldExpectsTrue() {
        ObjectFactory.generateOnMap(world, "Grass");

        assertTrue(Helper.isThereAnEmptyLocationInWorld(world, false));
    }

    @Test
    void isThereAnEmptyLocationInWorldDontAllowNonBlockingWtih1ObjectInWorldExpectsFalse() {
        int size = 1; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
        ObjectFactory.generateOnMap(world, "Grass");

        assertFalse(Helper.isThereAnEmptyLocationInWorld(world, true));
    }

    @Test
    void isThereAnEmptyLocationInWorldDontAllowNonBlockingWtihTwoObjectInWorldExpectsTrue() {
        for(int i = 0; i < 2; i++) {
            ObjectFactory.generateOnMap(world, "Grass");
        }
        
        assertTrue(Helper.isThereAnEmptyLocationInWorld(world, true));
    }

    @Test
    void testFilterByClassWithSuperClass(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, "Rabbit");
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, "Wolf");
        Carcass carcass = (Carcass) ObjectFactory.generateOnMap(world, "Carcass");

        Set<Entity> entities = new HashSet<>();
        entities.add(rabbit);
        entities.add(wolf);
        entities.add(carcass);

        assertEquals(2, Helper.filterByClass(entities, Animal.class).size());
    }

    @Test
    void findEmptyLocationWhereThereIsNoneExpectsNull() {
        int size = 1; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
        ObjectFactory.generateOnMap(world, "Rabbit");

        assertNull(Helper.findEmptyLocation(world));
    }

    @Test
    void findNonBlockingEmptyLocationWhereThereIsNoneExpectsNull() {
        int size = 1; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
        ObjectFactory.generateOnMap(world, "Grass");

        assertNull(Helper.findNonBlockingEmptyLocation(world));
    }

    @Test
    void findNonBlockingEmptyLocationWhereThereIsOneExpectsReturnOfLocation() {
        assertNotNull(Helper.findNonBlockingEmptyLocation(world));
    }

    @Test
    void findEmptyLocationWhereThereIsOneExpectsReturnOfLocation() {
        assertNotNull(Helper.findEmptyLocation(world));
    }
    
    @Test
    void testFindNearestOfExtendingClass() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Rabbit expected = (Rabbit) ObjectFactory.generateOnMap(world, "Rabbit");
        world.setCurrentLocation(new Location(0,0));
        Rabbit actual = (Rabbit) Helper.findNearest(world, wolf, 5, Organism.class);

        assertEquals(expected, actual);
    }
}