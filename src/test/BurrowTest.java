package test;

import Main.Hole;
import Main.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import Main.Burrow;

import java.util.LinkedList;
import java.util.List;

class BurrowTest {


    Program program;
    World world;
    Burrow burrow;

    Location location;

    @BeforeEach
    void setUp() {
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
        Location location = new Location(0,0);
        burrow = new Burrow(world,location);
    }

    @Test
    void testBurrowConstructorWithoutLocationArgumentWithNullWorldExpectsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Burrow(null);
        });
    }


    @Test
    void testBurrowConstructorWithLocationArgument() {


        Location expected = new Location(2,2);
        Burrow testBurrow = new Burrow(world, expected);
        List<Hole> holes = testBurrow.getEntries();
        Location actual = holes.get(0).getLocation();

        assertEquals(expected,actual);
    }

    @Test
    void testBurrowConstructorWithNullLocationArgument() {
        assertThrows(NullPointerException.class, () -> {
            new Burrow(world, null);
        });
    }

    @Test
    void testBurrowConstructorWithNullWorldArgument1() {
        assertThrows(NullPointerException.class, () -> {
            new Burrow(null);
        });
    }

    @Test
    void testBurrowConstructorWithNullWorldArgument2() {
        Location location = new Location(0,0);
        assertThrows(NullPointerException.class, () -> {
            new Burrow(null, location);
        });
    }

    @Test
    void testAddRabbit() {
        Rabbit rabbit = new Rabbit();
        burrow.addMember(rabbit);
        assertEquals(rabbit, burrow.getMembers().get(0));
    }

    @Test
    void testgetAdultRabbits0adults() {
        Rabbit rabbit = new Rabbit(2, burrow, false);
        burrow.addMember(rabbit);
        assertEquals(0, burrow.getAdultMembers().size());
    }

    @Test
    void testgetAdultRabbits1adults() {
        Rabbit rabbit = new Rabbit(2, burrow, false);
        Rabbit rabbit1 = new Rabbit(3, burrow, false);
        burrow.addMember(rabbit);
        burrow.addMember(rabbit1);
        assertEquals(1, burrow.getAdultMembers().size());
    }

    @Test
    void testgetAdultRabbits2adults() {
        Rabbit rabbit = new Rabbit(2, burrow, false);
        Rabbit rabbit1 = new Rabbit(3, burrow, false);
        Rabbit rabbit2 = new Rabbit(4, burrow, false);
        burrow.addMember(rabbit);
        burrow.addMember(rabbit1);
        burrow.addMember(rabbit2);
        assertEquals(2, burrow.getAdultMembers().size());
    }

    @Test
    void testClosestLocationIfThereIs1Entry() {
        Location location = new Location(1,1);
        Location rabbitLocation = new Location(0,0);
        Burrow burrow = new Burrow(world, location);
        assertEquals(location, burrow.findNearestEntry(rabbitLocation));
    }

    @Test
    void testClosestLocationIfThereIs2Entries() {
        Location location = new Location(2,2);
        Location location1 = new Location(1,1);
        Location rabbitLocation = new Location(0,0);
        Burrow burrow = new Burrow(world, location);
        burrow.addEntry(location1, world);
        assertEquals(location1, burrow.findNearestEntry(rabbitLocation));
    }

    @Test
    void testRemoveRabbit() {
        Rabbit rabbit1 = new Rabbit();
        Rabbit rabbit2 = new Rabbit();
        burrow.addMember(rabbit1);
        burrow.addMember(rabbit2);
        List<Rabbit> expected = new LinkedList<>();
        expected.add(rabbit1);
        burrow.removeMember(rabbit2);
        assertEquals(expected, burrow.getMembers());
    }

    @Test
    void addTestEntryExpectsThatEntryIsAddedToEntries() {
        System.out.println(burrow.getEntries());
        assertFalse(burrow.getEntries().isEmpty());
    }

    @AfterEach
    void tearDown() {
    }
}