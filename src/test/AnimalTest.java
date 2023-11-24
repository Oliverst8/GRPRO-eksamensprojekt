package test;

import Main.Burrow;
import Main.Grass;
import Main.ObjectFactory;
import Main.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {

    Program program;
    World world;
    Rabbit rabbit;

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




    @AfterEach
    void tearDown() {
    }
}
