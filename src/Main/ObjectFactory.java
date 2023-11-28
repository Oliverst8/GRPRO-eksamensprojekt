package Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.management.RuntimeErrorException;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class ObjectFactory {


    private ObjectFactory(){

    }

    public static Object generateOffMap(World world, String className, Object... constructorArgs){
        Object object = generateHelper(className, constructorArgs);
        world.add(object);
        return object;
    }

    public static Object generateOnMap(World world, String className, Object... constructorArgs) {

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Entity) {
            place(world, object);
        }

        return object;
    }

    public static Object generateOnMap(World world, Location location, String className, Object... constructorArgs) {

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Entity) {
            place(world, object, location);
        }
        
        return object;
    }


    private static Object generateHelper(String className, Object... constructorArgs) {
        className = className.toLowerCase();
        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        className = "Main." + className;

        try {
            Class<?> objectClass = Class.forName(className);

            Class<?>[] parameters = new Class[constructorArgs.length];
            for(int i = 0; i < parameters.length; i++){
                parameters[i] = constructorArgs[i].getClass();
            }

            Constructor<?> objectConstructor = objectClass.getConstructor(parameters);

            return objectConstructor.newInstance(constructorArgs);

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        } catch (NoSuchMethodException e) {
            System.out.println("No such constructor: " + className);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (IllegalAccessException e) {
            System.out.println("No access to constructor" + className);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (InvocationTargetException e) {
            System.out.println("Could not invoke constructor: " + className);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (InstantiationException e) {
            System.out.println("Could not instantiate class: " + className);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        // Failsafe
        throw new RuntimeErrorException(null, "Could not generate object: " + className);
    }

    private static void place(World world, Object object) {
        


        Location location;

        if(object instanceof NonBlocking) location = Helper.findNonBlockingEmptyLocation(world);
        else location = Helper.findEmptyLocation(world);


        world.setTile(location, object);
    }

    private static void place(World world, Object object, Location location) {
        world.setTile(location, object);
    }
}
