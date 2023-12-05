package test;

import Main.Grass;
import Main.Rabbit;
import Main.Wolf;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spawn.ObjectFactory;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    Program program;
    World world;
    Rabbit rabbit;

    double hungerMod;
    /**
     * Calls rabbit constructor that
     * Calls super constructor that creates an Animal Object
     */
    @BeforeEach
    void setUp() {
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!

        hungerMod = 10;

    }

    /**
     * Tests getCanEat from within Animal class
     * asserts the value to be the string array containing plant, fruit as canEat.
     */
    @Test
    void testAnimalConstructorCanEat(){
        rabbit = new Rabbit();
        assertTrue(rabbit.canIEat(Grass.class));
    }

    /**
     * Tests getHunger from within Animal class
     * assertEquals 50 as initialised in Animal Constructor
     */
    @Test
    void testAnimalConstructorHunger(){
        rabbit = new Rabbit();
        assertEquals(50, rabbit.getHunger());
    }


    @Test
    void testAnimalSetHunger(){
        rabbit = new Rabbit();
        rabbit.setHunger(hungerMod);
        assertEquals(10,rabbit.getHunger());
    }

    @Test
    void testAnimalAddHunger(){
        rabbit = new Rabbit();
        rabbit.addHunger(hungerMod);
        assertEquals(100,rabbit.getHunger());
    }

    @Test
    void testAnimalSleepsInsteadOfDyingWhenItHasZeroEnergyButOverZeroHunger(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
        wolf.setHunger(100);
        wolf.setHealth(world,100);
        wolf.setEnergy(0);
        program.simulate();
        program.simulate();
        program.simulate();
        int expectedSleepingCycles = world.getCurrentTime();
        int expectedEnergy = expectedSleepingCycles * 10;
        assertEquals(expectedEnergy, wolf.getEnergy());

    }
    @Test
    void testAnimalRemoveHunger(){
        rabbit = new Rabbit();
        double expectedHunger = rabbit.getHunger() - hungerMod;
        rabbit.removeHunger(hungerMod);
        assertEquals(expectedHunger,rabbit.getHunger());
    }
    @AfterEach
    void tearDown() {
    }
}
