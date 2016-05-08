package DataStructure;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class Pixel {
    private int id;
    private double verhoudingX, verhoudingY;
    private boolean idForced = false;           //  NECESSAIRY ?? CANT HE CHECK IF NUMBER IS LOGIC  TODO SEARCH OTHER SOLUTION
    private transient Color color;
    private int checkWidth, checkHeight, checkX, checkY;
    private transient ScreenSide screenSide;


    public Pixel() {
        this.color = Color.white;
        this.checkWidth = 100;
        this.checkHeight = 100;

    }
    public Pixel(int id){
        super();
        setId(id);
    }




    /* GETTERS AND SETTERS*/
    public void setLocation(int x, int y, int totalX, int totalY) {
        this.verhoudingX = (x / (double) totalX);
        this.verhoudingY = (y / (double) totalY);
    }


    public void setScreenSide(ScreenSide screenSide) {
        this.screenSide = screenSide;
    }

    public int berekenLocatieX(int width) {
        return (int) (width * verhoudingX);
    }

    public int berekenLocatieY(int height) {
        return (int) (height * verhoudingY);
    }


    public void parseSubImage(BufferedImage bufferedImage) {

        int red = 0;
        int green = 0;
        int blue = 0;
        int i = 0;

        for (int pixel : bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth())) {
            Color color = new Color(pixel);
            red += color.getRed();
            green += color.getGreen();
            blue += color.getBlue();
            i++;
        }

        this.color = new Color(red / i, green / i, blue / i);
    }

    public int[] getScreenDimension(int totalWidth, int totalHeight) {
        int[] dimension = new int[4];
        int x = berekenLocatieX(totalWidth);
        int y = berekenLocatieY(totalHeight);

        int size = 300;

        switch (screenSide.getRotation()) {
            case 0:
                y = size / 2;
                checkHeight = size;
                break;
            case 90:
                x = totalWidth - size / 2;
                checkWidth = size;
                break;
            case 180:
                y = totalHeight - size / 2;
                checkHeight = size;
                break;
            case 270:
                x = size / 2;
                checkWidth = size;
                break;
        }


        dimension[0] = x - checkWidth / 2;
        if (dimension[0] >= totalWidth) dimension[0] = totalWidth - 1;
        if (dimension[0] < 0) dimension[0] = 0;
        dimension[1] = y - checkHeight / 2;
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




}
