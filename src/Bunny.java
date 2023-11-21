import itumulator.executable.DisplayInformation;
import itumulator.world.World;

public class Bunny extends Animal {

    private Burrow burrow;
    private boolean inBurrow;

    /**
     * Initilises the food to the bunny can eat to plant and fruits
     * Initialises inBurrow to false
     */
    public Bunny(){
        super(new String[]{"Plant", "Fruit"});
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws IllegalArgumentException if world is null
     * Act check if its night or day
     * If its night:
     * - If it does not have a burrow it digs one where it is
     * - If it does have one it moves towards its burrow if it isnt in it, otherwise it does nothing
     * If its day:
     * - It has a chance to leave it burrow, the chance scales with hunger (The lower the hunger the higher the chance of leaving)
     * - If its in burrow chance to reproduce the chance scales with hunger (The higher the hunger the higher the chance of reproducing)
     * - If its in the burrow small chance to dig more entries to the burrow
     * - If out of borrow chance to move towards grass
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {

    }

    /**
     * Throws IllegalOperationException if dig is called when the bunny already has a burrow
     * If The bunny has at least 25 energy:
     * - calls makeBurrow()
     */
    private void dig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * - Makes a burrow at the current location
     *      *      * - Initialises burrow variable to newly created burrow
     *      *      * - Subtracts 25 energy
     */
    private void makeBurrow() {

    }

    /**
     * Throws IllegalOperationException if the bunny has no burrow
     * If the bunny does not have 50 energy return;
     * - calls makeBurrow()
     */
    private void expandBurrow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws IllegalOperationException if the bunny is in a burrow or if the bunny has no burrow
     * Throws IllegalArgumentException if world is null
     * Sets inBurrow to true
     * removes the bunny from the world
     */
    private void enterBurrow(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws IllegalOperationException if the bunny is not in a burrow or if the bunny has no burrow
     * Throws IllegalArgumentException if world is null
     * Sets inBurrow to false
     * Adds the bunny to the world in the location of a random burrow entry
     */
    private void exitBurrow(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws IllegalArgumentException if burrow is null
     * Throws IllegalOperationException if the bunny has a burrow already
     * Initializes the burrow to the argument
     * @param burrow The burrow which the bunny should make its own
     */
    private void setBurrow(Burrow burrow) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Return picture of bunny
     */
    @Override
    public DisplayInformation getInformation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
