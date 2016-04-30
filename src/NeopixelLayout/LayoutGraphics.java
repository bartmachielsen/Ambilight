package NeopixelLayout;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Bart on 30-4-2016.
 */
public class LayoutGraphics {
    private transient GraphicsDevice graphicsDevice;
    private ArrayList<Side> sides = new ArrayList<>();
    public LayoutGraphics(GraphicsDevice graphicsDevice){
       this.graphicsDevice = graphicsDevice;
    }

    public GraphicsDevice getGraphicsDevice() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    }

    public ArrayList<Side> getSides() {
        return sides;
    }

    public void setSides(ArrayList<Side> sides) {
        this.sides = sides;
    }
}

