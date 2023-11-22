package test;

import Main.Burrow;
import Main.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class OrganismTest {


    Program program;
    World world;
    Rabbit rabbit;

    /**
     * Calls rabbit constructor that
     * Calls super constructor that
     * Calls super constructor Organism that creates an organism object
     */
    @BeforeEach
    void setUp() {
        rabbit = new Rabbit();
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1000; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
    }

    /**
     *Tests die() from within Organism class
     *Asserts NoSuchElementException when trying to access getAge of the object
     */
    void testOrganismConstructorDie(){
        rabbit.die(world);
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            rabbit.getAge();
        });
    }
    /**
     * Tests getFoodType from within Organism class
     * assertEquals meat, if not it fails
     * Because foodtype set to meat in Animals Constructor
     */
    @Test
    void testOrganismConstructorFoodType(){
        Assertions.assertEquals("meat", rabbit.getFoodType());
    }

    /**
     * Tests getEnergy from within Organism class
     * assertEquals 100 as initialised in Organism Constructor
     */
    @Test
    void testOrganismConstructorEnergy(){
        Assertions.assertEquals(100, rabbit.getEnergy());
    }

    /**
     * Tests getAge from within Organism class
     * assertEquals 0 as initialised in Organism Constructor
     */
    @Test
    void testOrganismConstructorAge(){
        Assertions.assertEquals(0,rabbit.getAge());
    }


    @AfterEach
    void tearDown() {
    }
}
