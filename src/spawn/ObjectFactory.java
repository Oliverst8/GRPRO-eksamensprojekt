package spawn;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.management.RuntimeErrorException;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import main.Entity;
import main.Helper;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

public class ObjectFactory {
    private ObjectFactory() {}

    private static String[] requiresWorld = {"Bear", "Burrow"};

    /**
     * Generates an object of the given class name off the map, and returns it.
     * @param world the world the object is in.
     * @param className the name of the class of the object.
     * @param constructorArgs the arguments to pass to the constructor of the object.
     * @return the generated object.
     */
    public static Object generateOffMap(World world, String className, Object... constructorArgs) {
        if (Helper.doesArrayContain(requiresWorld, className)) {
            constructorArgs = prependArray(constructorArgs, world);
        }

        Object object = generateHelper(className, constructorArgs);

        if(object instanceof Entity) world.add(object);

        return object;
    }

    /**
     * Generates an object of the given class name on the map, and returns it.
     * @param world the world the object is in.
     * @param className the name of the class of the object.
     * @param constructorArgs the arguments to pass to the constructor of the object.
     * @return the generated object.
     */
    public static Object generateOnMap(World world, String className, Object... constructorArgs) {
        if (Helper.doesArrayContain(requiresWorld, className)) {
            constructorArgs = prependArray(constructorArgs, world);
        }

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Entity) place(world, object);

        return object;
    }

    /**
     * Generates an object of the given class name on the map, with the given location, and returns it.
     * @param world the world the object is in.
     * @param location the location of the object.
     * @param className the name of the class of the object.
     * @param constructorArgs the arguments to pass to the constructor of the object.
     * @return the generated object.
     */
    public static Object generateOnMap(World world, Location location, String className, Object... constructorArgs) {
        if (Helper.doesArrayContain(requiresWorld, className)) {
            constructorArgs = prependArray(constructorArgs, world);
        }
        
        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Entity) place(world, object, location);
        
        return object;
    }

    /**
     * Beforms the actual generation of the object.
     * @param className the name of the class of the object.
     * @param constructorArgs the arguments to pass to the constructor of the object.
     * @return the generated object.
     */
    private static Object generateHelper(String className, Object... constructorArgs) {
        className = "main." + className;

        try {
            Class<?> objectClass = Class.forName(className);

            Class<?>[] parameters = new Class[constructorArgs.length];
            for(int i = 0; i < parameters.length; i++){
                parameters[i] = convertToPrimitiveTypeIfThereIsOne(constructorArgs[i].getClass());
            }

            Constructor<?> objectConstructor = objectClass.getConstructor(parameters);

            return objectConstructor.newInstance(constructorArgs);

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        } catch (NoSuchMethodException e) {
            System.out.println("No such constructor in: " + className);
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

    /**
     * Places the object in the world.
     * @param world the world the object is in.
     * @param object the object to place.
     */
    private static void place(World world, Object object) {
        Location location;

        if(object instanceof NonBlocking) location = Helper.findNonBlockingEmptyLocation(world);
        else location = Helper.findEmptyLocation(world);

        world.setTile(location, object);
    }

    /**
     * Places the object in the world at the given location.
     * @param world the world the object is in.
     * @param object the object to place.
     * @param location the location to place the object at.
     */
    private static void place(World world, Object object, Location location) {
        world.setTile(location, object);
    }

    /**
     * Prepends the given object to the given array.
     * @param array the array to prepend to.
     * @param object the object to prepend.
     * @return the new array with the object prepended.
     */
    private static Object[] prependArray(Object[] array, Object object) {
        Object[] newArray = new Object[array.length + 1];
        newArray[0] = object;

        for(int i = 0; i < array.length; i++) {
            newArray[i + 1] = array[i];
        }

        return newArray;
    }

    /**
     * Converts the given class to its primitive type if there is one.
     * @param preConvertedClass the class to convert.
     * @return the converted class.
     */
    private static Class<?> convertToPrimitiveTypeIfThereIsOne(Class<?> preConvertedClass){
        Map<Class<?>, Class<?>> classMap = new HashMap<>();
        classMap.put(Integer.class, int.class);
        classMap.put(Boolean.class, boolean.class);
        classMap.put(Long.class, long.class);
        classMap.put(Short.class, short.class);
        classMap.put(Byte.class, byte.class);
        classMap.put(Float.class, float.class);
        classMap.put(Double.class, double.class);
        classMap.put(Character.class, char.class);
        return classMap.getOrDefault(preConvertedClass,preConvertedClass);
    }
}
