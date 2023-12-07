package test;

import Main.Cordyceps;
import Main.Fungi;
import Main.Rabbit;
import Main.Wolf;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spawn.ObjectFactory;

import static org.junit.jupiter.api.Assertions.*;
public class CordycepsTest {

    Program program;
    World world;

    @BeforeEach
    void setUp() {
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void testIfInfectedAnimalMovesTowardsAnimalOfSameTypeIfItsTheOnlyOne(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        rabbit.setInfected(new Cordyceps(rabbit));
        ((Rabbit) ObjectFactory.generateOnMap(world, new Location(2,2), "Rabbit")).skipTurn();
        program.simulate();
        assertEquals(new Location(1,1), world.getLocation(rabbit));
    }

    @Test
    void testIfInfectedAnimalMovesTowardsAnimalOfSameTypeIfThereAreTwoAnimals(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        rabbit.setInfected(new Cordyceps(rabbit));
        ((Rabbit) ObjectFactory.generateOnMap(world, new Location(2,2), "Rabbit")).skipTurn();
        ((Wolf) ObjectFactory.generateOnMap(world, new Location(1,2), "Wolf")).skipTurn();

        program.simulate();

        assertEquals(new Location(1,1), world.getLocation(rabbit));
    }

    @Test
    void testIfCordycepsGetsMoreEnergy(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        Fungi cordyceps = new Cordyceps(rabbit);
        world.add(cordyceps);

        rabbit.setInfected(cordyceps);

        program.simulate();

        assertEquals(5, cordyceps.getEnergy());
    }

    @Test
    void testIfCordycepsGetsMoreEnergyTwice(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        Fungi cordyceps = new Cordyceps(rabbit);
        world.add(cordyceps);

        rabbit.setInfected(cordyceps);

        program.simulate();
        program.simulate();

        assertEquals(10, cordyceps.getEnergy());
    }

    @Test
    void testIfCordycepsGoesToNewHostAndInfectsWhenOldHostDies(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world, new Location(2,2), "Rabbit");
        Cordyceps cordyceps = new Cordyceps(rabbit);
        world.add(cordyceps);

        rabbit.setInfected(cordyceps);
        program.simulate();
        rabbit.setHealth(0);
        program.simulate();

        assertFalse(world.contains(rabbit));
        assertEquals(rabbit2, cordyceps.getCurrentHost());
    }

    @Test
    void testIfCordycepsInfectsWhenOldHostDiesFromDistance(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world, new Location(2,2), "Rabbit");
        Cordyceps cordyceps = new Cordyceps(rabbit);
        world.add(cordyceps);

        rabbit.setInfected(cordyceps);
        rabbit.setHealth(0);
        program.simulate();


        assertFalse(world.contains(rabbit));
        assertEquals(rabbit2, cordyceps.getCurrentHost());
    }

}
