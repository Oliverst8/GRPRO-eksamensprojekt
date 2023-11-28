package test;

import Main.Burrow;
import Main.ObjectFactory;
import Main.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

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
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1000; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
        rabbit = new Rabbit();
    }

    /**
     *Tests die() from within Organism class
     *Asserts NoSuchElementException when trying to access getAge of the object
     */
    @Test
    void testOrganismDie(){
        Location rabbitLocation = new Location(1,1);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, rabbitLocation, "rabbit");
        rabbit.die(world);
        assertThrows(IllegalArgumentException.class, () -> {
            rabbit.die(world);
        });

    }
    /**
     * Tests getFoodType from within Organism class
     * assertEquals meat, if not it fails
     * Because foodtype set to meat in Animals Constructor
     */
    @Test
    void testOrganismFoodType(){
        Assertions.assertEquals("meat", rabbit.getFoodType());
    }

    /**
     * Tests getEnergy from within Organism class
     * assertEquals 100 as initialised in Organism Constructor
     */
    @Test
    void testOrganismEnergy(){
        Assertions.assertEquals(100, rabbit.getEnergy());
    }

    /**
     * Tests getAge from within Organism class
     * assertEquals 0 as initialised in Organism Constructor
     */
    @Test
    void testOrganismAge(){
        Assertions.assertEquals(0, rabbit.getAge());
    }

    @Test
    void testGrowOnce(){
        rabbit.grow();
        assertEquals(1,rabbit.getAge());
    }

    @Test
    void testGrowTwice(){
        rabbit.grow();
        rabbit.grow();
        assertEquals(2,rabbit.getAge());
    }

    @Test
    void testGetEnergy(){
        for (int i = 0; i < 12; i++) {
            rabbit.grow();
        }
    }

    @Test
    void dieWithNullArgumentExpectsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            rabbit.die(null);
        });
    }

    @Test
    void dieExpectsAnimalToBeRemoved() {
        Rabbit rabbit1 = null;
        try{
            rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world, "Rabbit");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        rabbit1.die(world);
        assertFalse(world.getEntities().containsKey(rabbit1));

    }

    @Test
    void getEnergyAdultOrganism() {
        rabbit.grow();
        rabbit.grow();
        rabbit.grow();
        assertEquals(100 - (rabbit.getEnergyLossPerDay()*(Math.max(0,rabbit.getAge()-rabbit.getAdultAge()))),rabbit.getEnergy());
    }

    @Test
    void getEnergyChildOrganism() {
        assertEquals(100 - (rabbit.getEnergyLossPerDay()*(Math.max(0,rabbit.getAge()-rabbit.getAdultAge()))),rabbit.getEnergy());
    }

    @Test
    void getEnergyFor12daysOld() {
        Rabbit rabbit = new Rabbit(12, new Burrow(world, new Location(0,0)), false);
        assertEquals(100 - (rabbit.getEnergyLossPerDay()*(Math.max(0,rabbit.getAge()-rabbit.getAdultAge()))),rabbit.getEnergy());
    }

    @AfterEach
    void tearDown() {
    }
}
