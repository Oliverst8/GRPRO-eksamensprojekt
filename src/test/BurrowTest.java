package test;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import Main.Hole;
import Main.Burrow;
import Main.Animal;
import Main.Rabbit;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BurrowTest {
    Program program;
    World world;
    Burrow burrow;

    Location location;

    @BeforeEach
    void setUp() {
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();

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
        List<Hole> holes = new ArrayList<>(testBurrow.getEntries());
        Location actual = holes.get(0).getLocation(world);

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
        List<Animal> members = new ArrayList<>(burrow.getMembers());

        assertEquals(rabbit, members.get(0));
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
        Burrow burrow = new Burrow(world, location);

        Location rabbitLocation = new Location(0,0);

        assertEquals(location, burrow.findNearestEntry(world, rabbitLocation));
    }

    @Test
    void testClosestLocationIfThereIs2Entries() {
        Location location = new Location(2,2);
        Burrow burrow = new Burrow(world, location);
        
        Location location1 = new Location(1,1);
        burrow.addEntry(world, location1);

        Location rabbitLocation = new Location(0,0);

        assertEquals(location1, burrow.findNearestEntry(world, rabbitLocation));
    }

    @Test
    void testRemoveRabbit() {
        Rabbit rabbit1 = new Rabbit();
        Rabbit rabbit2 = new Rabbit();
        burrow.addMember(rabbit1);
        burrow.addMember(rabbit2);
        Set<Animal> expected = new HashSet<>();
        expected.add(rabbit1);
        burrow.removeMember(rabbit2);

        assertEquals(expected, burrow.getMembers());
    }

    @Test
    void addTestEntryExpectsThatEntryIsAddedToEntries() {
        assertFalse(burrow.getEntries().isEmpty());
    }
}