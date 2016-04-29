package NeopixelLayout;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class Pixel {
    private Point location;
    public Pixel(Point location){
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }
}
