package test;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;
import java.util.ArrayList;

import main.*;

import spawn.Input;
import spawn.ObjectFactory;
import spawn.SpawningObject;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
        World world = generateWithInput(new Input("data/test/d4.txt"));
        assertTrue(world.getTile(new Location(0,0)).getClass().equals(Grass.class));
    }

    /**
     * Græs kan nedbryde og forsvinde
     *
     * setEnergy 10 so it low
     * setNight so grass doesnt make photosynthesis
     * Call program.simulate twice so it calls decay twice and removes 5 energy times two
     */
    @Test
    void K1_1b() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "Grass");
        assertTrue(world.contains(grass));

        grass.setEnergy(1);
        world.setNight();
        program.simulate();
        program.simulate();

        assertFalse(world.contains(grass));
    }

    /**
     * Græs kan sprede sig
     *
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
     *
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
        World world = generateWithInput(new Input("data/test/d5.txt"));
        assertTrue(world.getTile(new Location(0,0)).getClass().equals(Rabbit.class));
    }

    /**
     * Kaniner kan dø, hvilket resulterer I at de fjernes fra verdenen.
     *
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
     * Kaniner lever af græs som de spiser i løbet af dagen.
     */
    @Test
    void K1_2c_0(){
        Location location = new Location(0,0);
        ObjectFactory.generateOnMap(world, location,"Grass");
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");

        rabbit.setHunger(0);

        double hungerBeforeGrass = rabbit.getHunger();

        rabbit.setEnergy(1);
        program.simulate();

        assertTrue(rabbit.getHunger()>hungerBeforeGrass);
    }

    /**
     * Uden mad dør en kanin.
     */
    @Test
    void K1_2c_1(){
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");

        program.simulate();

        rabbit.setHunger(0);
        rabbit.setEnergy(1);

        program.simulate();
        program.simulate();

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

        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0),"Rabbit");
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,new Location(1,1), "Rabbit",5,new Burrow(world, new Location(1,1)),false);
        rabbit.addEnergy(rabbit.getMaxEnergy());
        rabbit1.addEnergy(rabbit1.getMaxEnergy());

        assertTrue(rabbit1.getEnergy() < rabbit.getEnergy());
    }

    /**
     * Kaniner kan reproducere.
     *
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
     * Kaniner kan grave huller
     *
     */
    @Test
    void K1_2f_0(){
        Location rabbit1Location = new Location(0,0);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit");
        rabbit1.setHunger(100);
        rabbit1.setEnergy(100);
        program.simulate();

        assertNotNull(rabbit1.getNest());
    }

    /**
     * Kaniner kan dele eksisterende huller med andre kaniner.
     *
     */
    @Test
    void K1_2f_1(){
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit",3,burrow,true);
        Location rabbit2Location = new Location(2,2);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit2Location, "Rabbit");
        rabbit2.setHunger(100);
        rabbit2.setEnergy(25); //cant dig burrow
        while(!rabbit2.isInNest()){
            rabbit1.skipTurn();
            program.simulate();
        }
        assertEquals(rabbit1.getNest(),rabbit2.getNest());
    }

    /**
     * Kaniner kan kun være knyttet til et hul.
     *
     */
    @Test
    void K1_2f_2(){
        Location rabbit1Location = new Location(0,0);
        Location rabbit2Location = new Location(3,3);

        Burrow burrow1 = new Burrow(world, rabbit1Location);
        Burrow burrow2 = new Burrow(world, rabbit2Location);

        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit2Location, "Rabbit",3,burrow1,false);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit",3,burrow2,false);

        rabbit1.setEnergy(25);
        rabbit2.setEnergy(25);

        double distanceBeforeRabbit1 = Utility.distance(world.getLocation(rabbit1),world.getLocation(burrow1.getEntries().iterator().next()));
        double distanceBeforeRabbit2 = Utility.distance(world.getLocation(rabbit2),world.getLocation(burrow2.getEntries().iterator().next()));

        world.setNight();
        program.simulate();

        double distanceAfterRabbit1 = Utility.distance(world.getLocation(rabbit1),world.getLocation(burrow1.getEntries().iterator().next()));
        double distanceAfterRabbit2 = Utility.distance(world.getLocation(rabbit2),world.getLocation(burrow2.getEntries().iterator().next()));

        assertTrue(distanceBeforeRabbit1>distanceAfterRabbit1);
        assertTrue(distanceBeforeRabbit2>distanceAfterRabbit2);
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
        Burrow burrow = new Burrow(world, new Location(3,3));
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
        World world = generateWithInput(new Input("data/test/d8.txt"));
        assertTrue(world.getTile(new Location(0,0)).getClass().equals(RabbitHole.class));
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

        Set<Location> possibleLocations = new HashSet<>(world.getEmptySurroundingTiles(world.getLocation(burrow.getEntries().iterator().next())));
        possibleLocations.add(rabbit1Location);

        program.simulate();

        assertTrue(possibleLocations.contains(world.getLocation(rabbit1)));
    }


    /**
     * Huller består altid minimum af en indgang.
     */
    @Test
    void KF1_1_0(){
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, rabbit1Location);
        assertEquals(burrow.getEntries().size(),1);
    }

    /**
     * Huller kan dog være flere indgange som sammen former én kanin tunnel.
     */
    @Test
    void KF1_1_1(){
        Location rabbit1Location = new Location(0,0);

        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit",3,burrow,true);

        rabbit1.setEnergy(100);
        rabbit1.setHunger(100);

        program.simulate();
        program.simulate();

        assertEquals(burrow.getEntries().size(),2);
    }

    /**
     * Kaniner kan kun grave nye udgange mens de er i deres huller.
     */
    @Test
    void KF1_1_2(){
        Location rabbit1Location = new Location(0,0);

        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world, rabbit1Location,"Rabbit",3,burrow,false);

        rabbit1.setEnergy(100);
        rabbit1.setHunger(99); // nok til at grave, ikke nok til at gå ind i burrow

        program.simulate();
        program.simulate(); //et par simulations

        assertEquals(burrow.getEntries().size(),1);
    }

    /**
     * Ulve kan placeres på kortet når input filerne beskriver dette.
     */
    @Test
    void K2_1a() {
        World world = generateWithInput(new Input("data/test/d2.txt"));
        assertTrue(world.getTile(new Location(0,0)).getClass().equals(Wolf.class));
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
        wolf.setEnergy(100);
        wolf.removeHealth(100,world);

        program.simulate();

        assertSame(world.getTile(location).getClass(), Carcass.class);
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
        double hungerBeforeConsuming = wolf.getHunger();

        program.simulate(); //4,4 - 3,3
        program.simulate(); //3,3 - 2,2
        program.simulate(); //2,2 - 1,1
        program.simulate(); // kill
        program.simulate(); //eat

        assertTrue(hungerBeforeConsuming<wolf.getHunger());
    }

    /**
     * Ulve er et flokdyr. De søger konstant mod andre ulve i flokken, og derigennem ’jager’ sammen.
     */
    @Test
    void K2_2a_0(){
        Wolf wolf1 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,1), "Wolf", wolf1.getPack(), 3, false);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (3,3),"Rabbit");

        rabbit.skipTurn();
        program.simulate();
        rabbit.skipTurn();
        program.simulate();

        Location wolf1Location = world.getLocation(wolf1);
        Location wolf2Location = world.getLocation(wolf2);
        Location predictedWolf1Location1 = new Location(2,1);
        Location predictedWolf1Locaiton2 = new Location(2,2);
        boolean isAtPredictedLocation = false;

        if(Objects.equals(wolf1Location, predictedWolf1Location1) || Objects.equals(wolf1Location, predictedWolf1Locaiton2)) {
            isAtPredictedLocation = true;
        }
        Location predictedWolf2Location = new Location(3,2);

        assertTrue(isAtPredictedLocation);
        assertEquals(predictedWolf2Location,wolf2Location);
    }

    /**
     * Når inputfilen beskriver (på en enkelt linje) at der skal placeres flereulve, bør disse automatisk være i samme flok.
     */
    @Test
    void K2_2a_1(){
        World world = generateWithInput(new Input("data/test/d9.txt"));

        Map<Object, Location> entities = world.getEntities();
        List<Wolf> wolfList = new ArrayList<>();
        int packsize = 0;

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Wolf.class)) {
                wolfList.add((Wolf) entity);
                packsize = ((Wolf) entity).getPack().getMembers().size();
            }
        }

        assertTrue(world.getEntities().size()==3);
        assertEquals(packsize,world.getEntities().size());
    }

    /**
     * Ulve og deres flok, tilhører en ulvehule, det er også her de formerer sig.
     */
    @Test
    void K2_3a_0(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf",5);
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", wolf.getPack(), 5, false);
        wolf.setHunger(100);
        wolf2.setHunger(100);
        program.simulate();//Dig
        program.simulate();//enter
        program.simulate();//recreate
        assertTrue(wolf.getPack().getMembers().size()>2);
    }

    /**
     *  Ulve ’bygger’ selv deres huler. Ulve kan ikke lide andre ulveflokke og deres huler.
     */
    @Test
    void K2_3a_1(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf",5);
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", wolf.getPack(), 5, false);
        wolf.setHunger(100);
        wolf2.setHunger(100);
        program.simulate();//Dig
        program.simulate();//enter

        Wolf wolf3 = (Wolf) ObjectFactory.generateOnMap(world, wolf.getPack().getDen().getLocation(world), "Wolf", 5);
        wolf.skipTurn();
        wolf2.skipTurn();

        program.simulate();

        assertTrue(Utility.distance(world.getLocation(wolf3),wolf.getPack().getDen().getLocation(world)) >= 1);
    }

    /**
     *  De prøver således at undgå andre grupper. Møder en ulv en ulv fra en anden flok, kæmper de mod hinanden.
     */
    @Test
    void K2_3a_2(){
        ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf",5);
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", 5);
        int healthBefore = wolf2.getHealth();

        program.simulate();

        assertTrue(healthBefore>wolf2.getHealth());
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
        World world = generateWithInput(new Input("data/test/d3.txt"));
        assertTrue(world.getTile(new Location(0,0)).getClass().equals(Bear.class));
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
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear",new Location(0,0));
        assertEquals(bear.getTerritory(),new Location(0,0));
    }

    /** 
     * Dertil spiser bjørne også bær fra buske (såsom blåbær og hindbær) når de gror i området.
     * Bær er en god ekstra form for næring for bjørnen (om end det ikke giver samme mængde energi som når de spiser kød)
     * men som det er med buske går der tid før bær gror tilbage.
     */
    @Test
    void K2_7a_0() {
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(2,1), "Bear");
        ObjectFactory.generateOnMap(world, "Berry");

        double bearHungerBefore = bear.getHunger();

        while(bear.getAge() < bear.getAdultAge()) {
            bear.grow();
        }

        for(int i = 0; i < 3; i++) {
            program.simulate();
        }

        assertTrue(bearHungerBefore < bear.getHunger());
    }

    /**
     * Bær skal indsættes på kortet når inputfilerne beskriver dette.
     */
    @Test
    void K2_7a_1() {
        World world = generateWithInput(new Input("data/demo/d12.txt"));
        Berry berryBush = (Berry) world.getTile(new Location(0,0));
        assertTrue(berryBush.containsBerries());
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
     * Dog er bjørnen ikke et flokdyr, og mødes kun med andre bjørne når de skal
     * parre sig. Bjørnen kan også dø og fjernes her fra verdenen.
     */
    @Test
    void KF2_2() {
        int size = 10; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        Bear bear2 = (Bear) ObjectFactory.generateOnMap(world, new Location(9,9), "Bear");

        while(!bear.isAdult()){
            bear.grow();
            bear2.grow();
        }

        while(world.getEntities().size() == 2){
            program.simulate();
        }

        assertTrue(2<world.getEntities().size());
    }

    /**
     * Bjørne der ikke er klar til at parre sig, angriber andre bjørne der bevæger sig ind
     * på deres områder. Tilsvarende angriber de andre dyr.
     */
    @Test
    void KF2_3() {
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        Bear bear2 = (Bear) ObjectFactory.generateOnMap(world, new Location(4,4), "Bear");
        double hungerBefore = bear.getHunger();
        while(world.contains(bear2)){
            bear.setHealth(bear.getMaxHealth());
            program.simulate();
        }
        program.simulate();
        assertTrue(hungerBefore<bear.getHunger());
    }

    /**
     * Opret ådsler, som placeres på kortet når input filerne beskriver dette.
     */
    @Test
    void K3_1a() {
        World world = generateWithInput(new Input("data/test/d7.txt"));
        assertTrue(world.getTile(new Location(0,0)).getClass().equals(Carcass.class));
    }

    /**
     * Når dyr dør nu, skal de efterlade et ådsel.
     */
    @Test
    void K3_1b_0(){
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "Rabbit");
        rabbit.removeHealth(100,world);

        assertTrue(world.getTile(location).getClass().equals(Carcass.class));
    }

    /**
     * Ådsler kan spises ligesom dyr kunne tidligere, dog afhænger mængden af ’kød’ af hvor stort dyret der døde er. Således
     * spises dyr ikke direkte længere når det slås ihjel, i stedet spises ådslet. Alle dyr som er
     * kødædende spiser ådsler
     */
    @Test
    void K3_1b_1(){

        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,1), "Wolf");

        rabbit.removeHealth(100,world);

        program.simulate();

        double wolfRabbitHunger = wolf.getHunger();

        wolf.removeHealth(wolf.getHealth(),world);

        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,2), "Wolf");

        program.simulate();

        double wolfWolfHunger = wolf2.getHunger();

        assertTrue(wolfRabbitHunger<wolfWolfHunger);
    }

    /**
     * Ådsler bliver dårligere med tiden og nedbrydes helt – selvom det ikke er spist
     * op (altså forsvinder det)! Det forsvinder naturligvis også hvis det hele er spist.
     */
    @Test
    void K3_1c() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        rabbit.setHealth(100);
        rabbit.removeHealth(100,world);

        program.simulate();

        Carcass carcass = (Carcass) world.getTile(new Location(0,0));

        carcass.setEnergy(carcass.getEnergy()-(carcass.getEnergy()-1));
        program.simulate();
        program.simulate();

        assertFalse(world.contains(carcass));
    }

    /**
     * Udover at ådsler nedbrydes, så hjælper svampene til. Således kan der opstå
     * svampe I et ådsel.
     *
     * Dette kan ikke ses på selve kortet, men svampen lever I selve ådslet.
     * Når ådslet er nedbrudt (og forsvinder), og hvis svampen er stor nok, kan den ses som
     * en svamp placeret på kortet, der hvor ådsle lå.
     */
    @Test
    void K3_2a_0() {
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        Bear bear2 = (Bear) ObjectFactory.generateOnMap(world, new Location(3,3), "Bear");
        bear.die(world);
        bear2.die(world);

        Carcass carcass = (Carcass) world.getTile(new Location(0,0));
        Carcass carcass2 = (Carcass) world.getTile(new Location(3,3));

        program.simulate();

        carcass2.setEnergy(10);

        while(world.contains(carcass)){
            program.simulate();
        }

        assertTrue(world.getTile(new Location(3,3)) == null);
        assertInstanceOf(Ghoul.class, world.getTile(new Location(0,0)));
    }

    /**
     * For at læse inputfilerne skal du sikre dig, at et ådsel kan indlæses med svamp.
     */
    @Test
    void k3_2a_1(){
        World world = generateWithInput(new Input("data/test/d11.txt"));
        Carcass carcass = (Carcass) world.getTile(new Location(0,0));
        assertTrue(carcass.isInfected());
    }

    /**
     * Svampe kan kun overleve, hvis der er andre ådsler den kan sprede sig til i
     * nærheden. Er dette ikke tilfældet, vil svampen også dø efter lidt tid. Desto større ådslet
     * er, desto længere vil svampen leve efter ådslet er væk. Da svampen kan udsende
     * sporer, kan den række lidt længere end kun de omkringliggende pladser.
     */
    @Test
    void K3_2b() {
        Carcass carcass = (Carcass) ObjectFactory.generateOnMap(world, new Location(2,2), "Carcass");
        ObjectFactory.generateOnMap(world, new Location(1,1), "Ghoul");

        program.simulate();

        assertTrue(carcass.isInfected());
    }

    /**
     * Når en svamp dør, er jorden ekstra gunstig. Derfor opstår græs på sådanne
     * felter, når svampen dør.
     */
    @Test
    void KF3_1a() {
        Ghoul ghoul = (Ghoul) ObjectFactory.generateOnMap(world, new Location(0,0), "Ghoul");
        ghoul.die(world);

        assertInstanceOf(Grass.class, world.getTile(new Location(0,0)));
    }

    /**
     * Ikke alle typer svampe lever på døde dyr. Der eksisterer også en anden type
     * svamp (Cordyceps).
     * Denne svamp spreder sig til, og styrer, (kun) levende dyr. Deres
     * livscyklus er den samme som de tidligere svampe; de nedbryder langsomt dyret, og er
     * der ikke mere tilbage af dyret, dør svampen snart efter. Når svampen har tæret nok på
     * dyret, dør dyret. Da denne svamp nedbryder dyret mens det lever, er der ikke noget
     * ådsel efter døden. Svampen dør også når dyret dør, med undtagelsen af krav
     */
    @Test
    void KF3_2a() {

        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");

        Cordyceps cordyceps = new Cordyceps();
        world.add(cordyceps);

        rabbit.setInfected(cordyceps);

        world.setCurrentLocation(world.getLocation(rabbit));

        rabbit.die(world);

        Set<Entity> surroundingEntities = Utility.getEntities(world, new Location(0,0), 3);

        surroundingEntities.add((Entity) world.getTile(new Location(0,0)));
        surroundingEntities = Utility.filterByClass(surroundingEntities, MycoHost.class);

        for(Entity animal : surroundingEntities){
            MycoHost animal1 = (MycoHost) animal;
            if(!animal1.isInfected()){
                surroundingEntities.remove(animal);
            }
        }

        assertFalse(world.contains(rabbit));
        assertTrue(surroundingEntities.isEmpty());

    }

    /**
     * Når Cordyceps’ vært dør, forsøger den at sprede sig til levende dyr i nærheden,
     * og kun på dette tidspunkt. Igen kan denne svamp sprede sig lidt længere end de
     * omkringliggende pladser da den sender sporer ud.
     */
    @Test
    void KF3_3a() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world, new Location(2,2), "Rabbit");
        Cordyceps cordyceps = new Cordyceps();
        world.add(cordyceps);

        rabbit.setInfected(cordyceps);
        rabbit.setHealth(0);
        program.simulate();


        assertFalse(world.contains(rabbit));
        assertEquals(cordyceps, rabbit2.getFungi());
    }

    /**
     * Når et dyr er inficeret med Cordyceps svampen, overtager svampen dyrets
     * handlinger. Dyret gør derfor som svampen bestemmer, hvilket er at søge mod andre
     * dyr af samme art.
     */
    @Test
    void KF3_3b() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        rabbit.setInfected(new Cordyceps());
        ((Rabbit) ObjectFactory.generateOnMap(world, new Location(2,2), "Rabbit")).skipTurn();
        ((Wolf) ObjectFactory.generateOnMap(world, new Location(1,2), "Wolf")).skipTurn();

        program.simulate();

        assertEquals(new Location(1,1), world.getLocation(rabbit));
    }

    /**
     *  Vælg et valgfrit dyr og implementer dets karakteristika og adfærd i økosystemet.
     *  Dyret skal have mindst et unikt adfærd.
     *
     *  Tester for hvorvidt om Turtles kan ligge æg
     */
    @Test
    void K4_1() {
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle", 3);
        Turtle turtle2 = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,1), "Turtle", 3);
        turtle.setEnergy(100);
        turtle2.setEnergy(100);
        world.setCurrentLocation(new Location(0,0));
        turtle.act(world);

        turtle.die(world);
        turtle2.die(world);
        Carcass carcass = (Carcass) world.getTile(new Location(0,0));
        Carcass carcass2 = (Carcass) world.getTile(new Location(0,1));
        carcass.die(world);
        carcass2.die(world);

        List<Object> entities2 = new ArrayList<>(world.getEntities().keySet());

        assertEquals(1, entities2.size());
        assertInstanceOf(Egg.class, entities2.get(0));
    }

    /**
     * Dyret skal kunne interagere med eksisterende elementer i økosystemet,
     * herunder ådsler, planter og andre dyr.
     */
    @Test
    void K4_2(){
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle", 3);
        ObjectFactory.generateOnMap(world,new Location(1,0),"Grass");

        double hungerBefore = turtle.getHunger();

        program.simulate();
        program.simulate();

        assertTrue(hungerBefore<turtle.getHunger());
    }

    /**
     * Simuler dyrets livscyklus, herunder fødsel, vækst, reproduktion og død.
     */
    @Test
    void K4_3_0(){
        //fødsel
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle", 3);
        Turtle turtle2 = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,1), "Turtle", 3);
        turtle.setEnergy(100);
        turtle2.setEnergy(100);
        world.setCurrentLocation(new Location(0,0));
        turtle.act(world);
        turtle.setEnergy(100);
        turtle2.setEnergy(100);
        turtle.act(world);

        turtle.die(world);
        turtle2.die(world);
        Carcass carcass = (Carcass) world.getTile(new Location(0,0));
        Carcass carcass2 = (Carcass) world.getTile(new Location(0,1));
        carcass.die(world);
        carcass2.die(world);

        assertEquals(world.getEntities().size(),2);
    }

    @Test
    void K4_3_1(){
        //HatchingFase
        Egg egg = (Egg) ObjectFactory.generateOnMap(world, new Location(0,0),"Egg", Turtle.class);

        while(world.contains(egg)){
            program.simulate();
        }

        assertInstanceOf(Turtle.class, world.getTile(new Location(0,0)));

    }

    @Test
    void K4_3_2(){
        //Opvækst
        Turtle turtleHatchedFromEgg = (Turtle) ObjectFactory.generateOnMap(world, new Location(1,1), "Turtle");

        while(!turtleHatchedFromEgg.isAdult()) {
            turtleHatchedFromEgg.setHunger(100);
            program.simulate();
        }

        assertTrue(turtleHatchedFromEgg.isAdult());
    }

    @Test
    void K4_3_3(){

        //reproducer
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(1,1), "Turtle");
        Turtle turtle2 = (Turtle) ObjectFactory.generateOnMap(world, new Location(1,0), "Turtle");

        while(!turtle.isAdult()) {
            turtle.setHunger(100);
            turtle2.setHunger(100);
            program.simulate();
        }

        while(world.getEntities().size() == 2) {
            turtle2.setEnergy(turtle2.getMaxEnergy());
            turtle.setEnergy(turtle.getMaxEnergy());
            program.simulate();
        }
        assertTrue(world.getEntities().size()>2);
    }

    @Test
    void K4_3_4(){
        //Indtil død
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(1,1), "Turtle");
        Turtle turtle2 = (Turtle) ObjectFactory.generateOnMap(world, new Location(1,0), "Turtle");

        int worldEntitiesBeforeDeath = 0;

        while(world.contains(turtle)||world.contains(turtle2)){
            program.simulate();
            worldEntitiesBeforeDeath = world.getEntities().size();
        }
        program.simulate();
        assertEquals(world.getEntities().size(),worldEntitiesBeforeDeath-2);
    }

    /**
     * Implementer dyrets fødekæde og prædator-bytte forhold.
     */
    @Test
    void K4_4(){
        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle", 3);
        ObjectFactory.generateOnMap(world,new Location(1,0),"Grass");

        double hungerBefore = turtle.getHunger();

        program.simulate();
        program.simulate();

        assertTrue(hungerBefore<turtle.getHunger());
    }

    /**
     * Dyrets tilstedeværelse og adfærd skal kunne påvirke økosystemets balance og
     * dynamik.
     *
     * Hvis dyr støder på dette dyr vil de forsøge at angribe det, men skildpadden kan gå ind i sin shell
     * Dyret vil dernæst opgive at blive ved med at angribe dyret og på den måde spilder dyret som minimum 1 tick
     * på noget det ikke engang får mad ud af.
     */
    @Test
    void K4_5(){

        Turtle turtle = (Turtle) ObjectFactory.generateOnMap(world, new Location(0,0), "Turtle");
        world.setCurrentLocation(new Location(0,0));

        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,1), "Bear");

        int expected = turtle.getShellHealth()-bear.getDamage();

        bear.act(world);

        assertEquals(expected, turtle.getShellHealth());
    }




    /**
     * Useful methods that gets used
     */

    /**
     * Used for testing with inputfiles
     * @param world
     * @param objects
     */
    private static void generateObjects(World world, ArrayList<SpawningObject> objects) {
        for (SpawningObject object : objects) {
            Pack pack = null;

            // Create pack if spawning object is a wolf
            if (object.getClassName().equals("Wolf")) pack = new Pack();

            for (int i = 0; i < object.getAmount(); i++) {
                Object newObject = null;

                // Generate object
                if (object.getClassName().equals("Wolf")) {
                    newObject = ObjectFactory.generateOnMap(world, object.getClassName(), pack);
                } else if(object.getLocation() != null) {
                    newObject = ObjectFactory.generateOnMap(world, object.getLocation(), object.getClassName(), object.getLocation());
                } else {
                    newObject = ObjectFactory.generateOnMap(world, object.getClassName());
                }

                // Set infected
                if (object.isInfected()) {
                    if (newObject instanceof Carcass) {
                        ((Carcass) newObject).setInfected(new Ghoul());
                    } else {
                        ((Animal) newObject).setInfected(new Cordyceps());
                    }
                }
            }
        }
    }

    private World generateWithInput(Input input) {
        int delay = 250;
        int display_size = 1000;
        int size = input.getSize();

        program = new Program(size, display_size, delay);
        World world = program.getWorld();
        generateObjects(world, input.getObjects());

        return world;


    }
}
