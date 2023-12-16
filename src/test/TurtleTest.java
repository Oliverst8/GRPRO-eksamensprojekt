package test;

import main.Bear;
import main.Turtle;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TurtleTest {

    Program program;
    World world;
    Turtle turtle;

    @BeforeEach
    void setUp(){
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle");
    }

    @Test
    void testThatTurtleGoesInShellDuringNight() {
        world.setNight();

        turtle.act(world);

        assertTrue(turtle.isInShell());
    }

    @Test
    void testThatTurtleExitsShellWhenItBecomesDayAfterNight() {
        world.setNight();

        turtle.act(world);
        assertTrue(turtle.isInShell());

        world.setDay();
        turtle.act(world);

        assertFalse(turtle.isInShell());
    }

    @Test
    void testRemoveHealthWithShellHealth() {
        int expected = turtle.getShellHealth() - 10;

        turtle.removeHealth(10, world);

        assertEquals(expected, turtle.getShellHealth());
    }

    @Test
    void testRemoveHealthWithoutShellHeath() {
        int expected = turtle.getHealth() - 10;

        turtle.removeHealth(turtle.getShellHealth(), world);
        turtle.removeHealth(10, world);

        assertEquals(expected, turtle.getHealth());
    }

    @Test
    void testThatTurtleDoesNotTakeDamageWhileInShell() {
        world.setCurrentLocation(new Location(0,0));

        ObjectFactory.generateOnMap(world, new Location(0,1), "Bear");

        int expected = turtle.getHealth();

        program.simulate();

        assertEquals(expected, turtle.getHealth());
    }

    @Test
    void testThatTurtleShellTakesDamageWhenAttacked() {
        world.setCurrentLocation(new Location(0,0));

        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,1), "Bear");

        int expected = turtle.getShellHealth()-bear.getStrength();

        bear.act(world);

        assertEquals(expected, turtle.getShellHealth());
    }

    @Test
    void testTurtleLeavesShellAfter3TurnsInIt() {
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,1), "Bear");
        world.setCurrentLocation(new Location(0,1));
        bear.act(world);

        for(int i = 0; i < 3; i++){
            assertTrue(turtle.isInShell());
            turtle.act(world);
        }

        assertFalse(turtle.isInShell());
    }

    @Test
    void testThatTurtleRunsAwayFormBear() {

        ObjectFactory.generateOnMap(world, new Location(0,1), "Bear");

        Location startingLocation = new Location(0,0);

        assertEquals(startingLocation, world.getLocation(turtle));

        world.setCurrentLocation(startingLocation);
        turtle.act(world);

        assertNotEquals(startingLocation, world.getLocation(turtle));

    }

    @Test
    void testIfTurtleLaysEggsWhenNextToOtherTurtle(){
        world.delete(turtle);

        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle", 3);
        Turtle turtle2 = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,1), "Turtle", 3);
        turtle.setEnergy(100);
        turtle2.setEnergy(100);

        assertEquals(2, world.getEntities().size());

        world.setCurrentLocation(new Location(0,0));
        turtle.act(world);

        assertEquals(3, world.getEntities().size());
        assertTrue(100 > turtle.getEnergy());
        assertTrue(100 > turtle2.getEnergy());
    }
}
