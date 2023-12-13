package main;

public abstract class Inanimate extends Entity {
    @Override
    public String getPNGPath() {
        return getType();
    }
}
