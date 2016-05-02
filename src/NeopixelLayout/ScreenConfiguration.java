package NeopixelLayout;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Bart on 30-4-2016.
 */
public class ScreenConfiguration {
    private transient GraphicsDevice graphicsDevice;
    private String graphicsID;
    private ArrayList<ScreenSide> screenSides = new ArrayList<>();

    public ScreenConfiguration(GraphicsDevice graphicsDevice) {
        this.graphicsDevice = graphicsDevice;
        this.graphicsID = graphicsDevice.getIDstring();
    }

    public GraphicsDevice getGraphicsDevice() {
        return graphicsDevice;
    }


    public void generateLight() {
        try {
            BufferedImage bufferedImage = (new Robot(graphicsDevice).createScreenCapture(graphicsDevice.getDefaultConfiguration().getBounds()));

            for (ScreenSide screenSide : screenSides) {
                for (Pixel pixel : screenSide.getPixels()) {
                    int[] points = pixel.getScreenDimension(bufferedImage.getWidth(), bufferedImage.getHeight());

                    pixel.parseSubImage(bufferedImage.getSubimage(points[0], points[1], points[2], points[3]));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reload() {
        for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            if (graphicsDevice.getIDstring().equals(graphicsID))
                this.graphicsDevice = graphicsDevice;
        }
    }

    public ArrayList<ScreenSide> getScreenSides() {
        return screenSides;
    }

    public void setScreenSides(ArrayList<ScreenSide> screenSides) {
        this.screenSides = screenSides;
    }
}

