package test;

import static org.junit.jupiter.api.Assertions.*;

import Main.Wolf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class WolfTest {

    Wolf wolf;

    @BeforeEach
    void setUp(){
        wolf = new Wolf();
    }

    @Test
    void testWolfConstructorWithoutPackExpectsNewPack() {
        assertNotNull(new Wolf().getPack());
    }

    @Test
    void testWolfConstructorWithPackExpectsNewPack() {
        assertEquals(wolf.getPack(), new Wolf(wolf.getPack()).getPack());
    }


    @AfterEach
    void tearDown() {
    }

}
