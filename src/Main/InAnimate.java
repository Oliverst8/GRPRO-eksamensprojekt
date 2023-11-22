package Main;

public abstract class InAnimate extends ObjectsOnMap{
    @Override
    protected String getPNGPath(){
        return getType();
    }

}
