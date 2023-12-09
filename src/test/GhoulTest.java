package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import Main.Ghoul;
import Main.Entity;
import Main.Rabbit;
import Main.Helper;
import Main.Carcass;

import spawn.ObjectFactory;

public class GhoulTest {

    Program program;
    World world;

    Carcass carcass;

    @BeforeEach
    void setUp() {
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        carcass = (Carcass) ObjectFactory.generateOnMap(world, new Location(2,2), "Carcass");


    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void testGhoulSpawnsInCarcussAfter53Steps() {

        for (int i = 0; i < 53; i++) {
            program.simulate();
            Set<Entity> entitiesInWorld = Helper.getEntities(world, new Location(2,2), 5);
            assertTrue(Helper.filterByClass(entitiesInWorld, Ghoul.class).isEmpty());
        }
        program.simulate();
        Set<Entity> entitiesInWorld = Helper.getEntities(world, new Location(2,2), 5);
        assertFalse(Helper.filterByClass(entitiesInWorld, Ghoul.class).isEmpty());

    }

    @Test
    void testGhoulSpreadsToNearbyCarcuss(){
        carcass.setInfected((Ghoul) ObjectFactory.generateOffMap(world, "Ghoul"));
        Carcass carcass1 = (Carcass) ObjectFactory.generateOnMap(world, new Location(1,1), "Carcass");


        program.simulate();

        Set<Object> entitiesInWorld = world.getEntities().keySet();
        Set<Entity> entities = new HashSet<>();

        for (Object entity: entitiesInWorld) {
            entities.add((Entity) entity);
        }


        assertEquals(2, Helper.filterByClass(entities, Ghoul.class).size());

    }

    @Test
    void testIfGhoulSpawnsInWorldWhenHostDies() {
        carcass.setInfected((Ghoul) ObjectFactory.generateOffMap(world, "Ghoul"));

        carcass.setEnergy(0);

        program.simulate();

        List<Object> entities = new ArrayList<>(world.getEntities().keySet());

        assertEquals(1, entities.size());
        assertInstanceOf(Ghoul.class, entities.get(0));
        assertInstanceOf(Ghoul.class, world.getTile(new Location(2,2)));

    }

    @Test
    void testIfGhoulSpreadsToOtherCarcassWhenItIsSpawnedItselfNextToIt() {
        ObjectFactory.generateOnMap(world, new Location(1,1), "Ghoul");

        program.simulate();

       assertTrue(carcass.isInfected());

    }

    @Test
    void testIfGhoulSpreadsToOtherCarcassWhenItIsSpawnedItself2BlocksAway() {
        ObjectFactory.generateOnMap(world, new Location(0,0), "Ghoul");

        program.simulate();

        assertTrue(carcass.isInfected());

    }

    @Test
    void testGhoulHasMoreEnergyDependingOnCarcassExpects10Energy() {



        Rabbit rabbit = new Rabbit();
        rabbit.setEnergy(10);

        carcass.setAnimal(rabbit);

        Ghoul ghoul = (Ghoul) ObjectFactory.generateOffMap(world, "Ghoul");
        carcass.setInfected(ghoul);



        for (int i = 0; i < 3; i++) {
            ghoul.skipTurn();
            program.simulate();
        }

        assertFalse(world.contains(carcass));
        assertEquals(10,ghoul.getEnergy());


    }

    @Test
    void testGhoulHasMoreEnergyDependingOnCarcassExpects100Energy() {


        Rabbit rabbit = new Rabbit();
        rabbit.setEnergy(100);

        carcass.setAnimal(rabbit);

        Ghoul ghoul = (Ghoul) ObjectFactory.generateOffMap(world, "Ghoul");
        carcass.setInfected(ghoul);


        for (int i = 0; i < 21; i++) {
            ghoul.skipTurn();
            program.simulate();
            System.out.println(ghoul.getEnergy() + " " + i);
        }

        assertFalse(world.contains(carcass));
        assertEquals(100, ghoul.getEnergy());

    }

    }
