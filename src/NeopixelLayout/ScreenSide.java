package NeopixelLayout;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class ScreenSide {
    private int rotation;
    private ArrayList<Pixel> pixels = new ArrayList<>();
    private boolean visible = true;


    public ScreenSide(int rotation) {
        this.rotation = rotation;
    }

    private int generateStapgrootte(int pixelAmount, int breedte, int i) {
        return (int) (((breedte - ((breedte / (double) pixelAmount)) * 1.5) / (double) pixelAmount) * i + ((int) (breedte / (double) pixelAmount)));
    }

    public void setPixelSize(int pixelSize) {
        while (pixels.size() < pixelSize) {
            pixels.add(new Pixel());
        }
        while (pixels.size() > pixelSize) {
            pixels.remove(pixels.size() - 1);
        }
    }

    public void generatePixelsDrawing(int width, int height) {
        int pixelAmount = pixels.size();
        Point location = new Point(0, 0);
        for (int i = 0; i < pixelAmount; i++) {
            switch (rotation) {
                case 0:
                    location.move(generateStapgrootte(pixelAmount, width, i), 25);
                    break;
                case 90:
                    location.move(width - 70, generateStapgrootte(pixelAmount, height, i));
                    break;
                case 180:
                    location.move(generateStapgrootte(pixelAmount, width, i), height - 50);
                    break;
                case 270:
                    location.move(25, generateStapgrootte(pixelAmount, height, i));
                    break;
            }
            pixels.get(i).setLocation((int) location.getX(), (int) location.getY(), width, height);

        }
    }


    public int giveNumber(int start, int... forced) {
        if (!visible) return start;
        for (int i = 0; i < pixels.size(); i++) {
            Pixel pixel = pixels.get(i);
            if (rotation == 180 || rotation == 270) {
                pixel = pixels.get((pixels.size() - 1) - i);
            }
            if (!pixel.isIdForced()) {
                for (int ii = 0; ii < forced.length; ii++) {
                    if (forced[ii] == start) {
                        start++;
                        ii = 0;
                    }
                }
                pixel.setId(start);
                start++;
            }
        }
        return start;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ArrayList<Pixel> getPixels() {
        if (visible) {
            return pixels;
        } else {
            return new ArrayList<>();
        }
    }
}
