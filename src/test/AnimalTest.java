package test;

import Main.Rabbit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {

    Rabbit rabbit;

    /**
     * Calls rabbit constructor that
     * Calls super constructor that creates an Animal Object
     */
    @BeforeEach
    void setUp() {
        rabbit = new Rabbit();
    }

    /**
     * Tests getCanEat from within Animal class
     * asserts the value to be the string array containing plant, fruit as canEat.
     */
    @Test
    void testAnimalConstructorCanEat(){
        Assertions.assertArrayEquals(new String[]{"plant","fruit"},rabbit.getCanEat());
    }

    /**
     * Tests getHunger from within Animal class
     * assertEquals 50 as initialised in Animal Constructor
     */
    @Test
    void testAnimalConstructorHunger(){
        Assertions.assertEquals(50, rabbit.getHunger());
    }




    @AfterEach
    void tearDown() {
    }
}
