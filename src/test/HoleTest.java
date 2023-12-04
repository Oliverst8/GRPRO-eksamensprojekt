package test;

import Main.Burrow;
import Main.Hole;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HoleTest {

    Program program;
    World world;

    Location location;
    Hole hole;

    Burrow burrow;
    @BeforeEach
    void setUp() {
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
        location = new Location(0,0);
        burrow = new Burrow(world, location);
        hole = new Hole(burrow, "hole-small");

    }

    /**
     * Tests Hole constructor with valid location
     * Expects the holes location to be the expectedLocation
     *  @Test
     *     void testHoleConstructorWithLocationArgument(){
     *         Location expectedLocation = new Location(60,60);
     *         Hole constructorHole = new Hole(expectedLocation, "hole-small");
     *         assertEquals(expectedLocation,constructorHole.getLocation());
     *     }
     */


    /**
     *  @Test
     *     void testHoleConstructorWithLocationAndBurrowArgument(){
     *         Location expectedLocation = new Location(60,60);
     *         Hole constructorHole = new Hole(expectedLocation,burrow, "hole-small");
     *         assertEquals(expectedLocation,constructorHole.getLocation());
     *         assertEquals(burrow,constructorHole.getBurrow());
     *     }
     */


    /**
     * Tests the holes getLocation()
     * Expects it to be the same as stated in the setup before each test
     * @Test
     *     void getLocation() {
     *         assertEquals(this.location,this.hole.getLocation());
     *     }
     */


    /**
     *
     */
    @AfterEach
    void tearDown() {
    }
}