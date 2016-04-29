package NeopixelLayout;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class Side {
    private int rotation;
    private ArrayList<Pixel> pixels = new ArrayList<>();

    Side(int rotation) {
        this.rotation = rotation;
    }

    private int generateStapgrootte(int pixelAmount, int breedte, int i) {
        return (int) (((breedte - ((breedte / (double) pixelAmount)) * 1.5) / (double) pixelAmount) * i + ((int) (breedte / (double) pixelAmount)));
    }

    public void generatePixelsDrawing(int width, int height, int pixelAmount) {
        boolean newPixel = true;
        while (pixels.size() < pixelAmount) {
            pixels.add(new Pixel());
        }
        while (pixels.size() > pixelAmount) {
            pixels.remove(pixels.size() - 1);
        }
        Point location = new Point(0, 0);
        for (int i = 0; i < pixelAmount; i++) {
            int tempSize = width;
            int stapSize;
            switch (rotation) {
                case 0:
                    location.move(generateStapgrootte(pixelAmount, width, i), 40);
                    break;
                case 90:
                    location.move(width - 70, generateStapgrootte(pixelAmount, height, i));
                    break;
                case 180:
                    location.move(generateStapgrootte(pixelAmount, width, i), height - 60);
                    break;
                case 270:
                    location.move(50, generateStapgrootte(pixelAmount, height, i));
                    break;
            }
            pixels.get(i).setLocation(location.getLocation(), width, height);

        }
    }


    public int giveNumber(int start) {
        for (int i = 0; i < pixels.size(); i++) {
            Pixel pixel = pixels.get(i);
            if (rotation == 180 || rotation == 270) {
                pixel = pixels.get((pixels.size() - 1) - i);
            }
            pixel.setId(start);
            start++;
        }
        return start;
    }

    public int getRotation() {
        return rotation;
    }

    public ArrayList<Pixel> getPixels() {
        return pixels;
    }
}
