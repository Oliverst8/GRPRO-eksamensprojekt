package test;

import Main.Grass;
import Main.Burrow;
import Main.Rabbit;
import Main.Consumable;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


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
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        rabbit = new Rabbit();
    }

    /**
     *Tests die() from within Organism class
     *Asserts NoSuchElementException when trying to access getAge of the object
     */
    @Test
    void testOrganismDie() {
        Location grassLocation = new Location(1,1);
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, grassLocation, "Grass");
        grass.die(world);

        assertThrows(IllegalArgumentException.class, () -> {
            grass.die(world);
        });
    }

    /**
     * Tests getFoodType from within Organism class
     * assertEquals meat, if not it fails
     * Because foodtype set to meat in Animals Constructor
     */
    @Test
    void testOrganismFoodType() {
        assertInstanceOf(Consumable.class, rabbit);
    }

    /**
     * Tests getEnergy from within Organism class
     * assertEquals 100 as initialised in Organism Constructor
     */
    @Test
    void testOrganismEnergy() {
        assertEquals(100, rabbit.getEnergy());
    }

    /**
     * Tests getAge from within Organism class
     * assertEquals 0 as initialised in Organism Constructor
     */
    @Test
    void testOrganismAge() {
        assertEquals(0, rabbit.getAge());
    }

    @Test
    void testGrowOnce() {
        rabbit.grow();
        assertEquals(1,rabbit.getAge());
    }

    @Test
    void testGrowTwice() {
        rabbit.grow();
        rabbit.grow();
        assertEquals(2,rabbit.getAge());
    }

    @Test
    void testGetEnergy() {
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
    void dieExpectsNONAnimalToBeRemoved() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "Grass");
        grass.die(world);

        assertFalse(world.contains(grass));
    }

    @Test
    void getEnergyAdultOrganism() {
        for(int i = 0; i < 3; i++) {
            rabbit.grow();
        }

        assertEquals(100 - (rabbit.getEnergyLossPerDay()*(Math.max(0,rabbit.getAge()-rabbit.getAdultAge()))),rabbit.getEnergy());
    }

    @Test
    void getEnergyChildOrganism() {
        assertEquals(100 - (rabbit.getEnergyLossPerDay()*(Math.max(0,rabbit.getAge()-rabbit.getAdultAge()))),rabbit.getEnergy());
    }

    @Test
    void getEnergyFor12daysOld() {
        Rabbit rabbit = new Rabbit(12, new Burrow(world, new Location(0,0)), false);
        rabbit.setEnergy(100);
        int expected = 100 - (rabbit.getEnergyLossPerDay()*(Math.max(0,rabbit.getAge()-rabbit.getAdultAge())));
        
        assertEquals(expected,rabbit.getEnergy());
    }

    @AfterEach
    void tearDown() {}
}
