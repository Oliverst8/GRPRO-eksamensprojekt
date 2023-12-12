package test;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import main.Bear;
import main.Turtle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import spawn.ObjectFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        turtle = (Turtle) ObjectFactory.generateOnMap(world, "Turtle");
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
    void testThatTurtleDoesNotTakeDamageWhileInShell() {

        world.delete(turtle);
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle");

        world.setCurrentLocation(new Location(0,0));
        turtle.act(world);

        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,1), "Bear");

        int expected = turtle.getHealth();

        program.simulate();

        assertEquals(expected, turtle.getHealth());
    }

    @Test
    void testThatTurtleShellTakesDamageWhenAttacked() {

        world.delete(turtle);
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle");

        world.setCurrentLocation(new Location(0,0));


        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,1), "Bear");

        int expected = turtle.getShellHealth()-bear.getStrength();

        bear.act(world);

        assertEquals(expected, turtle.getShellHealth());
    }

    @Test
    void testTurtleLeavesShellAfter3TurnsInIt() {
        world.delete(turtle);
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle");

        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,1), "Bear");
        world.setCurrentLocation(new Location(0,1));
        bear.act(world);

        for(int i = 0; i < 3; i++){
            assertTrue(turtle.isInShell());
            turtle.act(world);
        }

        assertFalse(turtle.isInShell());


    }

}
