package Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class ObjectFactory {
    public static Object generate(World world, String className, Object... constructorArgs) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Actor) {
            place(world, object);
        }

        return object;
    }

    public static Object generate(World world, Location location, String className, Object... constructorArgs) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Actor) {
            place(world, object, location);
        }
        
        return object;
    }

    //Skrevet i samarbejde med chatgpt
    private static Object generateHelper(String className, Object... constructorArgs) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        className = className.toLowerCase();
        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        className = "Main." + className;
        
        // Finds the type of class based on the className string
        Class<?> clazz = Class.forName(className);

        // Makes a list of the parameter types that are in the constructor
        Class<?>[] parameterTypes = new Class[constructorArgs.length];
        for (int i = 0; i < constructorArgs.length; i++) {
            parameterTypes[i] = constructorArgs[i].getClass();
        }
        //Find the constructor that has the matching parameters found earlier
        Constructor<?> constructor = clazz.getConstructor(parameterTypes);

        // Creates the an instance of the object, using the constructer arguments and returns it
        return constructor.newInstance(constructorArgs);
    }

    private static void place(World world, Object object) {
        
        Random r = new Random();

        Location location;

        do{    
            location = new Location(r.nextInt(world.getSize()),r.nextInt(world.getSize()));
        } while (!world.isTileEmpty(location) || (object instanceof NonBlocking && world.containsNonBlocking(location)));    


        world.setTile(location, object);
    }

    private static void place(World world, Object object, Location location) {
        world.setTile(location, object);
    }
}
