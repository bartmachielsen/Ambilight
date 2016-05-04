package DataStructure;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class Pixel {
    private int id;
    private double verhoudingX, verhoudingY;
    private boolean idForced = false;           //  NECESSAIRY ?? CANT HE CHECK IF NUMBER IS LOGIC  TODO SEARCH OTHER SOLUTION
    private transient Color color;
    private int checkWidth, checkHeight;


    public Pixel() {
        this.color = Color.white;
        this.checkWidth = 100;
        this.checkHeight = 100;


    }



    /* GETTERS AND SETTERS*/
    public void setLocation(int x, int y, int totalX, int totalY) {
        this.verhoudingX = (x / (double) totalX);
        this.verhoudingY = (y / (double) totalY);
    }

    public void setCheck(int width, int height) {
        this.checkHeight = height;
        this.checkWidth = width;
    }

    public int berekenLocatieX(int width) {
        return (int) (width * verhoudingX);
    }

    public int berekenLocatieY(int height) {
        return (int) (height * verhoudingY);
    }


    public void parseSubImage(BufferedImage bufferedImage) {
        ArrayList<Color> colors = new ArrayList<>();
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int ii = 0; ii < bufferedImage.getHeight(); ii++) {
                colors.add(getARGB(bufferedImage.getRGB(i, ii)));
            }
        }
        for (Color colorTemp : colors) {
            red += colorTemp.getRed();
            green += colorTemp.getGreen();
            blue += colorTemp.getBlue();
            alpha += colorTemp.getAlpha();
        }


        this.color = new Color(red / colors.size(), green / colors.size(), blue / colors.size(), alpha / colors.size());
    }

    private Color getARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new Color(red, green, blue, alpha);
    }

    public int[] getScreenDimension(int totalWidth, int totalHeight) {
        int[] dimension = new int[4];


        dimension[0] = berekenLocatieX(totalWidth) - checkWidth / 2;
        if (dimension[0] >= totalWidth) dimension[0] = totalWidth - 1;
        if (dimension[0] < 0) dimension[0] = 0;
        dimension[1] = berekenLocatieY(totalHeight) - checkHeight / 2;
        if (dimension[1] >= totalHeight) dimension[1] = totalHeight - 1;
        if (dimension[1] < 0) dimension[1] = 0;
        dimension[2] = checkWidth;

        if (dimension[0] + dimension[2] >= totalWidth) dimension[2] = totalWidth - dimension[0];
        dimension[3] = checkHeight;
        if (dimension[1] + dimension[3] >= totalHeight) dimension[3] = totalHeight - dimension[1];


        return dimension;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isIdForced() {
        return idForced;
    }

    public void setIdForced(boolean idForced) {
        this.idForced = idForced;
    }



    public Shape generateEllipseArea(int width, int height) {
        return new Ellipse2D.Double(berekenLocatieX(width) - 37, berekenLocatieY(height) - 37, 150, 150);

    }
}
