package test;

import Main.Grass;
import Main.Rabbit;

import itumulator.world.World;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

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
    void testAnimalRemoveHunger() {
        rabbit.removeHunger(hungerMod);
        assertEquals(0,rabbit.getHunger());
    }
    @AfterEach
    void tearDown() {}
}
