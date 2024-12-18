package error;

import main.Animal;

public class CantReproduceException extends Exception {
    private final Animal animal1;
    private final Animal animal2;
    
    public CantReproduceException(Animal animal1, Animal animal2) {
        super();
        this.animal1 = animal1;
        this.animal2 = animal2;
    }

    /**
     * Prints information about the animal to the console.
     * @param animal the animal that caused the exception.
     */
    private void printInformation(Animal animal) {
        System.out.println(animal + " age: " + animal.getAge() + " and has an adult age of: " + animal.getAdultAge());
        System.out.println(animal + " is of species: " + animal.getType());
        System.out.println(animal + " has an energy amount of: " + animal.getEnergy());
    }

    /**
     * Prints the animals that caused the exception to the console.
     */
    public void printInformation() {
        System.out.println(animal1 + " " + animal2 + " cant reproduce");
        printInformation(animal1);
        printInformation(animal2);
    }
}
