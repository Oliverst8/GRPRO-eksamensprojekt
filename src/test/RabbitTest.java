package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Main.Rabbit;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

class RabbitTest {

    Rabbit rabbit;

    /**
     * Calls rabbit constructor and creates a new Rabbit object
     */
    @BeforeEach
    void setUp() {
        rabbit = new Rabbit();
    }

    /**
     * Tests if NullPointerException is throwed when calling Rabbit.act() with Null
     * Asserts NullpointerException
     */
    @Test
    void testActWithNullArgumentExpectsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            rabbit.act(null);
        });
    }

    @Test
    void testConstructorAdultAge(){
        assertEquals(3, rabbit.getAdultAge());
    }

    /**
     * Tests isInBurrow
     * Should return false as initialised in the Rabbit constructor
     */
    @Test
    void testRabbitConstructorInBurrowExpectsFalse(){
        Assertions.assertFalse(rabbit.isInBurrow());
    }

    @AfterEach
    void tearDown() {
    }
}