package main;

import java.awt.Color;

public class WolfHole extends Hole {
    public WolfHole() {}

    @Override
    public String getType() {
        return "hole-large";
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }
}
