package test;

import main.Bear;
import main.Wolf;
import main.Grass;
import main.Rabbit;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        rabbit = new Rabbit();
        hungerMod = 10;

    }

    /**
     * Tests getCanEat from within Animal class
     * asserts the value to be the string array containing plant, fruit as canEat.
     */
    @Test
    void testAnimalConstructorCanEat() {
        assertTrue(rabbit.canIEat(Grass.class));
    }

    /**
     * Tests getHunger from within Animal class
     * assertEquals 50 as initialised in Animal Constructor
     */
    @Test
    void testAnimalConstructorHunger() {
        assertEquals(50, rabbit.getHunger());
    }

    @Test
    void testAnimalSetHunger() {
        rabbit.setHunger(hungerMod);
        assertEquals(10,rabbit.getHunger());
    }

    @Test
    void testAnimalAddHunger() {
        rabbit.addHunger(hungerMod);
        assertEquals(100,rabbit.getHunger());
    }

    @Test
    void testAnimalSleepsInsteadOfDyingWhenItHasZeroEnergyButOverZeroHunger() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
        wolf.setHunger(100);
        wolf.setHealth(100);
        wolf.setEnergy(0);
        program.simulate();
        program.simulate();
        program.simulate();
        int expectedSleepingCycles = world.getCurrentTime();
        int expectedEnergy = expectedSleepingCycles * 10;
        assertEquals(expectedEnergy, wolf.getEnergy());
    }

    @Test
    void testAnimalRemoveHunger() {
        double expectedHunger = rabbit.getHunger() - hungerMod;
        rabbit.removeHunger(hungerMod);
        assertEquals(expectedHunger,rabbit.getHunger());
    }
    @Test
    void testAnimalEatTheLargerTheAnimalTheMoreHungerConsumedByAnimal() {
        int size = 20; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        Wolf wolf1 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        ObjectFactory.generateOnMap(world, new Location(1,1), "Wolf", wolf1.getPack(), 3, false);
        ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", wolf1.getPack(), 3, false);
        Wolf wolf4 = (Wolf) ObjectFactory.generateOnMap(world, new Location(15,15), "Wolf");
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (14,14),"Rabbit");
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(3,3), "Bear");

        wolf1.setHunger(50);
        wolf4.setHunger(50);
        bear.removeHealth(bear.getHealth(), world);
        rabbit.removeHealth(rabbit.getHealth(),world);

        program.simulate();
        program.simulate();
        program.simulate();

        assertTrue(wolf4.getHunger()<wolf1.getHunger());
    }
}
