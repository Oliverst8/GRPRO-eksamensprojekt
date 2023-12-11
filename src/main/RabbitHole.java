package main;

public class RabbitHole extends Hole {
    private final Burrow burrow;

    /**
     * Initialises the hole belonging to a burrow
     * @param burrow the burrow the hole belongs to
     */
    public RabbitHole(Burrow burrow) {
        this.burrow = burrow;
    }

    /**
     * @return the image of the hole
     */
    @Override
    public String getType() {
        return "hole-small";
    }

    /**
     * @return the burrow the hole belongs to
     */
    public Burrow getBurrow() {
        return burrow;
    }
}
