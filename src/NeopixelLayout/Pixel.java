package NeopixelLayout;

import java.awt.*;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class Pixel {
    private Point location;
    private int id;
    private double verhoudingX, verhoudingY;

    // NECESARIYLY????
    private Shape shape;


    /* GETTERS AND SETTERS*/


    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location, int totalX, int totalY) {
        this.location = location;
        this.verhoudingX = (location.getX() / (double) totalX);
        this.verhoudingY = (location.getY() / (double) totalY);
    }


    public int berekenLocatieX(int width) {
        return (int) (width * verhoudingX);
    }

    public int berekenLocatieY(int height) {
        return (int) (height * verhoudingY);
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
