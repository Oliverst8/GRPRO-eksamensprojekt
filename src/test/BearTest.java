package test;

import Main.Bear;
import Main.Wolf;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class BearTest {
    Program program;
    World world;
    Bear bear;

    @BeforeEach
    void setUp() {
        int size = 5;
        int delay = 1;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        bear = new Bear(world);
    }

    @Test
    void testBearAttackAnimalIfInTerritory() {
        Bear bear = (Bear) ObjectFactory.generateOnMap(world,new Location(0, 0), "Bear");
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0, 1), "Wolf");
        
        int initialWolfHealth = wolf.getHealth();

        for(int i = 0; i < bear.getAdultAge(); i++) {
            bear.skipTurn();
            wolf.skipTurn();
            bear.grow();
        }
        
        wolf.skipTurn();
        program.simulate();
        program.simulate();

        assertTrue(wolf.getHealth() < initialWolfHealth || !world.contains(wolf));
    }

    @Test
    void testBearDoNotAttackAnimalIfNotInTerritory() {
        ObjectFactory.generateOnMap(world,new Location(0, 0), "Bear");
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(world.getSize() - 1, world.getSize() - 1), "Wolf");
        
        int initialWolfHealth = wolf.getHealth();
        
        wolf.skipTurn();
        program.simulate();

        assertEquals(wolf.getHealth(), initialWolfHealth);
    }

    @AfterEach
    void tearDown() {}
}