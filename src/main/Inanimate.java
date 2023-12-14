package main;

import itumulator.executable.DynamicDisplayInformationProvider;

public abstract class Inanimate extends Entity implements DynamicDisplayInformationProvider {
    @Override
    public String getPNGPath() {
        return getType();
    }
}
