package main;

import java.awt.Color;

public class RabbitHole extends Hole {
    private final Burrow burrow;

    /**
     * Initialises the hole belonging to a burrow
     * @param burrow the burrow the hole belongs to
     */
    public RabbitHole(Burrow burrow) {
        this.burrow = burrow;
    }

    @Override
    public String getType() {
        return "hole-small";
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    /**
     * @return the burrow the hole belongs to
     */
    public Burrow getBurrow() {
        return burrow;
    }
}
