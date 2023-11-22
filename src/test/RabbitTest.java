package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Main.Rabbit;

import static org.junit.Assert.assertThrows;

class RabbitTest {

    Rabbit rabbit;

    @BeforeEach
    void setUp() {
        rabbit = new Rabbit();
    }

    @Test
    void testActWithNullArgumentExpectsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            rabbit.act(null);
        });
    }

    @Test
    void testRabbitConstructorInBurrowExpectsFalse(){
        Assertions.assertFalse(rabbit.isInBurrow());
    }

    @Test
    void testRabbitConstructorCanEat(){
        Assertions.assertArrayEquals(new String[]{"plant","fruit"},rabbit.getCanEat());
    }

    @Test
    void testRabbitConstructorAge(){
        Assertions.assertEquals(0, rabbit.getAge());
    }

    @Test
    void testRabbitConstructorHunger(){
        Assertions.assertEquals(50, rabbit.getHunger());
    }

    @Test
    void testRabbitConstructorFoodType(){
        Assertions.assertEquals("meat", rabbit.getFoodType());
    }

    @Test
    void testRabbitConstructorEnergy(){
        Assertions.assertEquals(100, rabbit.getEnergy());
    }


    @AfterEach
    void tearDown() {
    }
}