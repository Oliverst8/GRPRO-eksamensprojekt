package Main;

public class cantReproduceException extends Exception{
    private final Animal animal1;
    private final Animal animal2;
    public cantReproduceException(Animal animal1, Animal animal2){
        super();
        this.animal1 = animal1;
        this.animal2 = animal2;
    }

    private void printInformation(Animal animal){
        System.out.println(animal + " age: " + animal.getAge() + " and has an adult age of: " + animal.getAdultAge());
        System.out.println(animal + " is of species: " + animal.getType());
        System.out.println(animal + " has an energy amount of: " + animal.getEnergy());
    }

    public void printInformation(){
        System.out.println(animal1 + " " + animal2 + " cant reproduce");
        printInformation(animal1);
        printInformation(animal2);

    }
}
