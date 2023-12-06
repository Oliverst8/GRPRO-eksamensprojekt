package test;


import Main.Helper;
import Main.NoEmptyLocationException;
import Main.Organism;
import Main.Rabbit;
import Main.Wolf;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import spawn.ObjectFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {

    Program program;
    World world;

    @BeforeEach
    void setUp() {
        int size = 4; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1000; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
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
    
    @Test
    void testFindNearestOfExtendingClass(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Rabbit expected = (Rabbit) ObjectFactory.generateOnMap(world, "Rabbit");
        world.setCurrentLocation(new Location(0,0));
        Rabbit actual = (Rabbit) Helper.findNearest(world, wolf, 5, Organism.class);

        assertEquals(expected, actual);
    }

}