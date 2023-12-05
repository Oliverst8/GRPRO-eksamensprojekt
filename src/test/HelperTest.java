package test;

import Main.Helper;
import Main.NoEmptyLocationException;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {

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
    void findEmptyLocationWhereThereIsNoneExpectsNoEmptyLocationException() {
        ObjectFactory.generateOnMap(world, "Rabbit");

        assertThrows(NoEmptyLocationException.class, () -> {
           Helper.findEmptyLocation(world);
        });
    }

    @Test
    void findNonBlockingEmptyLocationWhereThereIsNoneExpectsNoEmptyLocationException() {
        ObjectFactory.generateOnMap(world, "Grass");

        assertThrows(NoEmptyLocationException.class, () -> {
            Helper.findNonBlockingEmptyLocation(world);
        });
    }

    @Test
    void findNonBlockingEmptyLocationWhereThereIsOneExpectsReturnOfLocation() {
        assertNotNull(Helper.findNonBlockingEmptyLocation(world));
    }

    @Test
    void findEmptyLocationWhereThereIsOneExpectsReturnOfLocation() {
        assertNotNull(Helper.findEmptyLocation(world));
    }

    @AfterEach
    void tearDown() {}
}