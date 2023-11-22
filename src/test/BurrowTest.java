package test;

import Main.Hole;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Main.Burrow;

import java.util.ArrayList;
import java.util.List;

class BurrowTest {


    Program program;
    World world;
    Burrow burrow;

    @BeforeEach
    void setUp() {
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1000; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        Program program = new Program(size, display_size, delay); // opret et nyt program
        World world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
    }

    @Test
    void testBurrowConstructorWithoutLocationArgumentWithNullWorldExpectsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Burrow(null);
        });
    }

    @Test
    void testBurrowConstructorWithoutLocationArgument() {
        assertNotNull(new Burrow(world).getEntries());
    }

    @Test
    void testBurrowConstructorWithLocationArgument() {
        List<Hole> expected = new ArrayList<>();
        Location location = new Location(0,0);
        expected.add(new Hole(location));
        assertEquals(expected, new Burrow(world, location).getEntries());


    }

    @Test
    void addTestEntryWith() {
    }

    @AfterEach
    void tearDown() {
    }


}