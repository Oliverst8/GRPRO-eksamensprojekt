package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Main.Bunny;

class BunnyTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testBunnyConstructorInBurrowExpectsFalse(){
        Assertions.assertFalse(new Bunny().isInBurrow());
    }

    @Test
    void testBunnyConstructorCanEat(){
        Assertions.assertArrayEquals(new String[]{"Main.Plant","Fruit"},new Bunny().getCanEat());
    }

    @Test
    void testBunnyConstructorAge(){
        Assertions.assertEquals(0, new Bunny().getAge());
    }

    @Test
    void testBunnyConstructorHunger(){
        Assertions.assertEquals(50, new Bunny().getHunger());
    }

    @Test
    void testBunnyConstructorFoodType(){
        Assertions.assertEquals("Meat", new Bunny().getFoodType());
    }

    @Test
    void testBunnyConstructorEnergy(){
        Assertions.assertEquals(100, new Bunny().getEnergy());
    }


    @AfterEach
    void tearDown() {
    }
}