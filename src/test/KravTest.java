package test;

import java.util.ArrayList;
import java.util.Map;

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
        while(world.isNight()){
            program.simulate();
        }
        program.simulate();
        rabbit.addEnergy(100);
        }
        System.out.println(rabbit.getAge());
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
        World world = generateWithInput(new Input("data/demo/d8.txt"));

        boolean containsHole = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Hole.class)) {
                containsHole = true;
                break;
            }
        }

        assertTrue(containsHole);
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
        World world = generateWithInput(new Input("data/demo/d2.txt"));

        boolean containsWolf = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Wolf.class)) {
                containsWolf = true;
                break;
            }
        }

        assertTrue(containsWolf);
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
        int wolf3HealthBefore = 200;
        int wolf3HealthAfter = 200;


        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf",5);
        wolf.setHunger(100);

        program.simulate();

        if(wolf.isInNest()){
            wolf.setHunger(50);
            Wolf wolf2 = null;

            program.simulate();

            if(!wolf.isInNest()) {
                wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,0), "Wolf", wolf.getPack(), 5, false);
                wolf.setHunger(100);
                wolf2.setHunger(100);

                program.simulate();
                program.simulate();
                program.simulate();
                wolf.setEnergy(20);
                wolf2.setEnergy(20);
                wolf.setHunger(50);
                wolf2.setHunger(50);
            }

            if(world.getEntities().size()>=4) {

                Wolf wolf3 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
                wolf3.setHealth(200);
                wolf3HealthBefore = wolf3.getHealth();

                wolf3.skipTurn();
                program.simulate();
                wolf3.skipTurn();
                program.simulate();
                wolf3.skipTurn();
                program.simulate();

                wolf3HealthAfter = wolf3.getHealth();
            }
        }
        assertTrue(wolf3HealthBefore>wolf3HealthAfter);
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

        //Kunne placere en bjørn 0,0 og dernæst to kaniner en indenfor den radius og en udenfor og efter 6 simulates se at den anden ikke dør
    }

    /** 
     * Dertil spiser bjørne også bær fra buske (såsom blåbær og hindbær) når de gror i området.
     * Bær er en god ekstra form for næring for bjørnen (om end det ikke giver samme mængde energi som når de spiser kød)
     * men som det er med buske går der tid før bær gror tilbage.
     * Bær skal indsættes på kortet når inputfilerne beskriver dette.
     */
    @Test
    void K2_7a() {
        boolean bearHasEaten = false;
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(2,1), "Bear");
        Berry berry = (Berry) ObjectFactory.generateOnMap(world, "Berry");

        double before = 0;


        world.setNight();
        while(world.isNight()) {
            program.simulate();
        }


        for(int i = 0; i<3;i++) {
            program.simulate();
        }



        if(before < bear.getHunger()) {
            bearHasEaten = true;
        }

        assertTrue(bearHasEaten);
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
     * Opret ådsler, som placeres på kortet når input filerne beskriver dette.
     */
    @Test
    void K3_1a() {
        World world = generateWithInput(new Input("data/demo/d7.txt"));

        boolean containsCarcass = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Carcass.class)) {
                containsCarcass = true;
                break;
            }
        }

        assertTrue(containsCarcass);
    }

    /**
     * Når dyr dør nu, skal de efterlade et ådsel. Ådsler kan spises ligesom dyr kunne
     * tidligere, dog afhænger mængden af ’kød’ af hvor stort dyret der døde er. Således
     * spises dyr ikke direkte længere når det slås ihjel, i stedet spises ådslet. Alle dyr som er
     * kødædende spiser ådsler
     */
    @Test
    void K3_1b() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "Rabbit");
        rabbit.removeHealth(100,world);

        double wolfRabbitHunger = 0;
        double wolfWolfHunger = 0;

        if(world.getTile(location).getClass().equals(Carcass.class)){
            Location wolfLocation = new Location(1,1);
            Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, wolfLocation, "Wolf");
            program.simulate();
            wolfRabbitHunger = wolf.getHunger();
            if(world.isTileEmpty(location)){
                wolf.removeHealth(wolf.getHealth(),world);
                Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,2), "Wolf");
                program.simulate();
                wolfWolfHunger = wolf2.getHunger();
            }
        }
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

        for(int i = 0; i <= 100; i++){
            program.simulate();
        }

        assertFalse(world.contains(carcass));
    }

    /**
     * Udover at ådsler nedbrydes, så hjælper svampene til. Således kan der opstå
     * svampe I et ådsel.
     * Dette kan ikke ses på selve kortet, men svampen lever I selve ådslet.
     * Når ådslet er nedbrudt (og forsvinder), og hvis svampen er stor nok, kan den ses som
     * en svamp placeret på kortet, der hvor ådsle lå. For at læse inputfilerne skal du sikre
     * dig, at et ådsel kan indlæses med svamp.
     */
    @Test
    void K3_2a() {

    }

    /**
     * Svampe kan kun overleve, hvis der er andre ådsler den kan sprede sig til i
     * nærheden. Er dette ikke tilfældet, vil svampen også dø efter lidt tid. Desto større ådslet
     * er, desto længere vil svampen leve efter ådslet er væk. Da svampen kan udsende
     * sporer, kan den række lidt længere end kun de omkringliggende pladser.
     */
    @Test
    void K3_2b() {

    }


    /**
     * Når en svamp dør, er jorden ekstra gunstig. Derfor opstår græs på sådanne
     * felter, når svampen dør.
     */
    @Test
    void KF3_1a() {

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

    }

    /**
     * Når Cordyceps’ vært dør, forsøger den at sprede sig til levende dyr i nærheden,
     * og kun på dette tidspunkt. Igen kan denne svamp sprede sig lidt længere end de
     * omkringliggende pladser da den sender sporer ud.
     */
    @Test
    void KF3_3a() {

    }

    /**
     * Når et dyr er inficeret med Cordyceps svampen, overtager svampen dyrets
     * handlinger. Dyret gør derfor som svampen bestemmer, hvilket er at søge mod andre
     * dyr af samme art.
     */
    @Test
    void KF3_3b() {

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
