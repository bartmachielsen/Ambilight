package NeopixelLayout;

import java.awt.*;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class Pixel {
    private Point location;
    private int id;

    // NECESARIYLY????
    private Shape shape;


    /* GETTERS AND SETTERS*/

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
