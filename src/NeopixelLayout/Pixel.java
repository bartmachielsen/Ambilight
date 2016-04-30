package NeopixelLayout;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class Pixel {
    private int id;
    private double verhoudingX, verhoudingY;
    private boolean idForced = false;

    // NECESARIYLY????
    private Shape shape;


    private Shape pixelArea;

    /* GETTERS AND SETTERS*/


    public void setLocation(int x, int y, int totalX, int totalY) {
        this.verhoudingX = (x / (double) totalX);
        this.verhoudingY = (y / (double) totalY);
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


    public boolean isIdForced() {
        return idForced;
    }

    public void setIdForced(boolean idForced) {
        this.idForced = idForced;
    }

    public Shape getPixelArea() {
        return pixelArea;
    }

    public Shape generateEllipseArea(int width, int height) {
        return new Ellipse2D.Double(berekenLocatieX(width) - 37, berekenLocatieY(height) - 37, 150, 150);

    }
}
