package test;

import java.util.ArrayList;
import java.util.Map;

import Main.Hole;
import Main.Wolf;
import Main.Bear;
import Main.Grass;
import Main.Burrow;
import Main.Entity;
import Main.Rabbit;
import Main.Carcass;

import spawn.Input;
import spawn.ObjectFactory;
import spawn.SpawningObject;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KravTest {
    Program program;
    World world;

    @BeforeEach
    void setUp() {
        int size = 5; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }

    /**
     * Græs kan blive plantet når input filerne beskriver dette. Græs skal blot tilfældigt placeres.
     */
    @Test
    void K1_1a() {
        World world = generateWithInput(new Input("data/demo/d4.txt"));

        boolean containsGrass = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Grass.class)) {
                containsGrass = true;
                break;
            }
        }

        assertTrue(containsGrass);
    }

    /**
     * Græs kan nedbryde og forsvinde
     * setEnergy 10 so it low
     * setNight so grass doesnt make photosynthesis
     * Call program.simulate twice so it calls decay twice and removes 5 energy times two
     */
    @Test
    void K1_1b() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "Grass");
        grass.setEnergy(10);
        world.setNight();
        program.simulate();
        program.simulate();
        assertFalse(world.contains(grass));
    }

    /**
     * Græs kan sprede sig
     * Creates grass, sets its energy to 75 which is required in its dayBehaviour to spread
     */
    @Test
    void K1_1c() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "Grass");
        grass.setEnergy(75);
        program.simulate();
        assertEquals(2,world.getEntities().size());
    }

    /**
     * Dyr kan stå på græs uden der sker noget med græsset.
     * Create both object at the same place
     * Set rabbit to skip turn so it doesnt eat the grass.
     * assert true that their locations equal eachother
     */
    @Test
    void K1_1d() {
        Location location = new Location(0,0);
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, location,"Grass");
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");

        rabbit.skipTurn();
        program.simulate();

        assertEquals(world.getLocation(grass),world.getLocation(rabbit));
    }

    /**
     * Kaniner kan placeres på kortet når input filerne beskriver dette. Kaniner skal blot tilfældigt placeres.
     */
    @Test
    void K1_2a() {
        World world = generateWithInput(new Input("data/demo/d5.txt"));

        boolean containsRabbit = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Rabbit.class)) {
                containsRabbit = true;
                break;
            }
        }

        assertTrue(containsRabbit);
    }

    /**
     * Kaniner kan dø, hvilket resulterer I at de fjernes fra verdenen.
     * Spawn rabbit on world
     * set energy and hunger to 0 so it call die in daybehaviour
     * Assert that the object doesnt exist within the world
     */
    @Test
    void K1_2b() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world,"Rabbit");
        rabbit.setHunger(0);
        rabbit.setEnergy(0);
        program.simulate();
        assertFalse(world.contains(rabbit));
    }

    /**
     * Kaniner lever af græs som de spiser i løbet af dagen, uden mad dør en kanin.
     * Create rabbit and grass. Observe if rabbit eats grass by comparing hunger
     * while(world doesnt contain grass and world contains rabbit) program.simulate
     * Assert that rabbit doesnt exist within the world.
     */
    @Test
    void K1_2c() {
        Location location = new Location(0,0);
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, location,"Grass");
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");
        double hungerBeforeGrass = rabbit.getHunger();
        program.simulate();
        if(hungerBeforeGrass<rabbit.getHunger()){
            while(!world.contains(grass) && world.contains(rabbit)){
                program.simulate();
            }
        }
        assertFalse(world.contains(rabbit));
    }

    /**
     * Kaniners alder bestemmer hvor meget energi de har.
     * Creates rabbit
     * Gets the energy from when it was age 0
     * When it gets older than adultage the energy begins to fall
     * For loop ages rabbit and by the end of the loop adds 100 energy
     * Asserts that the energy at age 0 (100) is higher than rabbit.getenergy after even tho it gets 100 energy added
     */
    @Test
    void K1_2d() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");

        int energyBefore = rabbit.getEnergy(); //100

        for(int i = 0; i<rabbit.getAdultAge()+1 ; i++){
        world.setNight();
        program.simulate();
        world.setDay();
        program.simulate();
        rabbit.addEnergy(100);
        }

        assertTrue(energyBefore>rabbit.getEnergy());
    }

    /**
     * Kaniner kan reproducere.
     * Generates two rabbits. Gives them enough energy to call reproduce
     * Expects that burrow.getMembers.size gets larger by 1
     */
    @Test
    void K1_2e() {
        Burrow burrow = new Burrow(world, new Location(0,0));
        Rabbit rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        rabbitInsideBurrow.setEnergy(100);
        rabbit2.setEnergy(100);
        int expected = burrow.getMembers().size()+1;
        program.simulate();

        assertEquals(expected,burrow.getMembers().size());
    }

    /**
     * Kaniner kan grave huller, eller dele eksisterende huller med andre kaniner. Kaniner kan kun være knyttet til et hul.
     * Rabbit bliver spawnet, hopper ned i eget hul.
     * Rabbit graver et nyt hul
     * ________________________
     * Hvis flere huller eksisterer end dem vi instantierede så vi instantierer vi rabbit2
     * Så længe at rabbit2 ikke er i en burrow vil den simulerer, den energi er sat til 25 så den kan ikke grave sin egen
     * Når den er i en burrow tjekkes noget nyt
     * ________________________
     * Hvis mængden af alle indgange til burrowen er ligeså stor som mængden af alle entities på kortet minus 2 (de to kaniner)
     * Har vi sikret os at den ikke har gravet sin egen burrow
     * Nu lader vi den så grave sit eget hul, ved at sætte dens energi til 100 og skipturn for rabbit1
     * Vi har tallet fra før programmet blev simuleret på antallet af indgange for den burrow, nu har vi det nye
     * Hvis det nye er højere end det gamle, sæt boolean til true
     * ________________________
     * Hvis true kør alle entities igennem for hvis hullet findes i entities i++
     * ________________________
     * World entities - 2 == i
     */
    @Test
    void K1_2f() {
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit",3,burrow,false);
        Rabbit rabbit2 = null;
        rabbit1.setHunger(100);
        rabbit1.setEnergy(100);
        System.out.println(world.getEntities());
        program.simulate(); //Enters burrow
        program.simulate(); //dig another hole

        if(world.getEntities().size()>2){
            Location rabbit2Location = new Location(2,2);
            rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit2Location, "Rabbit");
            rabbit2.setHunger(100);
            rabbit2.setEnergy(25); //cant dig burrow
            while(!rabbit2.isInNest()){
                rabbit1.skipTurn();
                program.simulate();
            }
        }

        boolean rabbit2DigsAnotherHole = false;
        if(burrow.getEntries().size()==world.getEntities().size()-2 && rabbit2.isInNest()){
            int currentEntries = burrow.getEntries().size();
            rabbit1.skipTurn();
            rabbit2.setEnergy(100);
            program.simulate();
            if(currentEntries<burrow.getEntries().size()){
                rabbit2DigsAnotherHole = true;
            }
        }
        int i = 0;

        if(rabbit2DigsAnotherHole){
            for(Hole hole : burrow.getEntries()){
                if(world.getEntities().containsKey(hole)) i++;
            }
        }

        assertEquals(i, world.getEntities().size()-2);
    }

    /**
     * Kaniner søger mod deres huller når det bliver aften, hvor de sover.
     * Burrow har koordinat 2,2
     * Kanin har 0,0
     * Det bliver nat og den søger hen imod
     * Kanin bør derfor nu have lokationen 1,1
     */
    @Test
    void K1_2g() {
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, new Location(2,2));
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit",3,burrow,false);
        world.setNight();
        program.simulate();
        Location forventetLocation = new Location(1,1);
        assertEquals(forventetLocation,world.getLocation(rabbit1));
    }

    /**
     * Huller kan enten blive indsat når input filerne beskriver dette, eller graves af kaniner. Huller skal blot blive tilfældigt placeret når de indgår i en input fil.
     */
    @Test
    void K1_3a() {

    }

    /**
     * Dyr kan stå på et kaninhul uden der sker noget.
     * Spawnlocation for både burrow(med tilhørende hul) og rabbit er den samme
     * Vi simulerer programmet og verificerer at de begge stadig har samme lokation
     */
    @Test
    void K1_3b() {
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit",3,burrow,false);
        rabbit1.setEnergy(100);
        rabbit1.setHunger(99);

        program.simulate();

        assertEquals(world.getLocation(burrow.getEntries().iterator().next()),world.getLocation(rabbit1));
    }

    /**
     * Huller består altid minimum af en indgang, der kan dog være flere indgange som sammen former én kanin tunnel. Kaniner kan kun grave nye udgange mens de er i deres huller.
     * Kaninen har nok energi til at grave et hul mere, men er ikke nede i burrow da den har 99 i hunger og skal jage
     * Hvis den ikke er i burrow, simuler, sethunger 100, simuler
     * Hvis den nu er i burrow, noter entries før
     * Simuler
     * Assert that entries before is less than burrow.getEntries.size at the current moment
     */
    @Test
    void KF1_1() {
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit",3,burrow,false);
        rabbit1.setEnergy(100);
        rabbit1.setHunger(99);
        program.simulate();
        if(!rabbit1.isInNest()){
            program.simulate();
            rabbit1.setHunger(100);
            program.simulate();
        }

        int entriesBefore = 0;
        if(rabbit1.isInNest()){
            entriesBefore = burrow.getEntries().size();
            program.simulate();
        }
        assertTrue(burrow.getEntries().size()>entriesBefore);
    }

    /**
     * Ulve kan placeres på kortet når input filerne beskriver dette.
     */
    @Test
    void K2_1a() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        assertTrue(world.contains(wolf));
    }

    /**
     * Ulve kan dø, hvilket resulterer I at de fjernes fra verdenen.
     * Spawn ulv, remove health, simulate og ulven dør
     * Bliver lavet om til en carcass, hvis den gør det så sættes indeholdercarcass til 1
     * assert that world.getentities.size == indeholdercarcass
     */
    @Test
    void K2_1b() {
        Location location = new Location(0,0);
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, location, "Wolf");
        wolf.setHealth(100);
        wolf.removeHealth(100,world);
        wolf.setEnergy(100);
        program.simulate();
        int indeholderCarcass = 0;
        if(world.getTile(location).getClass() == Carcass.class){
            indeholderCarcass++;
        }
        assertEquals(world.getEntities().size(), indeholderCarcass);
    }

    /**
     * Ulve jager andre dyr og spiser dem for at opnå energi
     * If wolf movestowards rabbit location log hunger before
     * Simulate three times
     * Asserttrue that hungerbefore is lower than hunger after
     */
    @Test
    void K2_1c() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(4,4), "Wolf");
        ObjectFactory.generateOnMap(world,new Location(0,0), "Rabbit");
        program.simulate(); //4,4 - 3,3
        program.simulate(); //3,3 - 2,2
        double hungerBeforeConsuming = wolf.getHunger();
        Location expectedLocation = new Location(2,2);
        if(world.getLocation(wolf).equals(expectedLocation)){
            program.simulate();
            program.simulate();
            program.simulate();
        }
        assertTrue(hungerBeforeConsuming<wolf.getHunger());
    }

    /**
     * Ulve er et flokdyr. De søger konstant mod andre ulve i flokken, og derigennem ’jager’ sammen.
     * Når inputfilen beskriver (på en enkelt linje) at der skal placeres flereulve, bør disse automatisk være i samme flok.
     */
    @Test
    void K2_2a() {

    }

    /**
     * Ulve og deres flok, tilhører en ulvehule, det er også her de formerer sig.
     * Ulve ’bygger’ selv deres huler. Ulve kan ikke lide andre ulveflokke og deres huler.
     * De prøver således at undgå andre grupper. Møder en ulv en ulv fra en anden flok, kæmper de mod hinanden.
     */
    @Test
    void K2_3a() {

    }

    /**
     * Kaniner frygter ulve og forsøger så vidt muligt at løbe fra dem.
     */
    @Test
    void K2_4a() {
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,new Location(1,1), "Rabbit");
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Location assumedRabbitLocation = new Location(2,2);
        wolf.skipTurn();
        program.simulate();

        assertEquals(assumedRabbitLocation,world.getLocation(rabbit1));
    }

    /**
     * Bjørne kan placeres på kortet når input filerne beskriver dette.
     */
    @Test
    void K2_5a() {
        World world = generateWithInput(new Input("data/demo/d3.txt"));

        boolean containsBear = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Bear.class)) {
                containsBear = true;
                break;
            }
        }

        assertTrue(containsBear);
    }

    /**
     * Bjørne jager ligesom ulve, og spiser også alt.
     */
    @Test
    void K2_5b() {
        ObjectFactory.generateOnMap(world, new Location(2,2), "Wolf");
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        program.simulate();
        assertEquals(new Location(1,1),world.getLocation(bear));
    }

    /**
     * Kaniner frygter bjørne og forsøger så vidt muligt at løbe fra dem.
     */
    @Test
    void K2_5c() {
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,new Location(1,1), "Rabbit");
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        Location assumedRabbitLocation = new Location(2,2);
        bear.skipTurn();
        program.simulate();

        assertEquals(assumedRabbitLocation,world.getLocation(rabbit1));
    }

    /**
     * Bjørnen er meget territoriel, og har som udgangspunkt ikke et bestemt sted den ’bor’.
     * Den knytter sig derimod til et bestemt område og bevæger sig sjældent ud herfra.
     * Dette territories centrum bestemmes ud fra bjørnens startplacering på kortet
     */
    @Test
    void K2_6a() {

    }

    /** 
     * Dertil spiser bjørne også bær fra buske (såsom blåbær og hindbær) når de gror i området.
     * Bær er en god ekstra form for næring for bjørnen (om end det ikke giver samme mængde energi som når de spiser kød)
     * men som det er med buske går der tid før bær gror tilbage.
     * Bær skal indsættes på kortet når inputfilerne beskriver dette.
     */
    @Test
    void K2_7a() {

    }

    /**
     * Bjørnen er naturligvis vores øverste rovdyr i denne lille fødekæde, men det
     * hænder at en stor nok gruppe ulve kan angribe (og dræbe) en bjørn.
     * Dette vil i praksis være hvis flere ulve af samme flok er i nærheden af en bjørn.
     */
    @Test
    void K2_8a() {
        Wolf wolf1 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,1), "Wolf", 5);
        ObjectFactory.generateOnMap(world, new Location(1,1), "Wolf", wolf1.getPack(), 3, false);
        ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", wolf1.getPack(), 3, false);
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(3,3), "Bear");

        int bearHealthBeforeAtt = bear.getHealth();
        bear.skipTurn();
        program.simulate();
        bear.skipTurn();
        program.simulate();
        bear.skipTurn();
        program.simulate();

        int bearHealthAfterAtt = bear.getHealth();
        assertTrue(bearHealthBeforeAtt>bearHealthAfterAtt);
    }

    /**
     * Hvis den ene ulv bliver voldsomt såret, underkaster den sig den sejrende ulvs flok. En såret ulv har brug for hvile før den kan fortsætte.
     */
    @Test
    void KF2_1() {

    }

    /**
     * Dog er bjørnen ikke et flokdyr, og mødes kun med andre bjørne når de skal
     * parre sig. Bjørnen kan også dø og fjernes her fra verdenen.
     */
    @Test
    void KF2_2() {

    }

    /**
     * Bjørne der ikke er klar til at parre sig, angriber andre bjørne der bevæger sig ind
     * på deres områder. Tilsvarende angriber de andre dyr.
     */
    @Test
    void KF2_3() {

    }

    /**
     * Useful methods that gets used
     */
    private void generateObjects(World world, ArrayList<SpawningObject> objects) {
        for (SpawningObject object : objects) {
            for (int i = 0; i < object.getAmount(); i++) {
                if(object.getLocation() != null) {
                    ObjectFactory.generateOnMap(world, object.getLocation(), object.getClassName());
                } else {
                    ObjectFactory.generateOnMap(world, object.getClassName());
                }
            }
        }
    }

    private World generateWithInput(Input input) {
        int delay = 250;
        int display_size = 1000;
        int size = input.getSize();

        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();
        generateObjects(world, input.getObjects());
        return world;
    }
}
