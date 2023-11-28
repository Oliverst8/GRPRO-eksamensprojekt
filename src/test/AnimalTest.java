package test;

import Main.Rabbit;
import itumulator.executable.Program;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        rabbit = new Rabbit();
        hungerMod = 10;

    }

    /**
     * Tests getCanEat from within Animal class
     * asserts the value to be the string array containing plant, fruit as canEat.
     */
    @Test
    void testAnimalConstructorCanEat(){
        assertArrayEquals(new String[]{"plant","fruit"},rabbit.getCanEat());
    }

    /**
     * Tests getHunger from within Animal class
     * assertEquals 50 as initialised in Animal Constructor
     */
    @Test
    void testAnimalConstructorHunger(){
        assertEquals(50, rabbit.getHunger());
    }


    @Test
    void testAnimalSetHunger(){

        rabbit.setHunger(hungerMod);
        assertEquals(10,rabbit.getHunger());
    }

    @Test
    void testAnimalAddHunger(){
        rabbit.addHunger(hungerMod);
        assertEquals(100,rabbit.getHunger());
    }

    @Test
    void testAnimalRemoveHunger(){
        rabbit.removeHunger(hungerMod);
        assertEquals(0,rabbit.getHunger());
    }
    @AfterEach
    void tearDown() {
    }
}
