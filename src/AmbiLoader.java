import ArduinoConnector.ArduinoConnector;
import NeopixelLayout.Configuration;
import NeopixelLayout.Pixel;
import NeopixelLayout.ScreenConfiguration;
import NeopixelLayout.ScreenSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * Created by Bart Machielsen on 1-5-2016.
 */
public class AmbiLoader extends Thread implements ActionListener {
    private Timer ambitimer;
    private int FRAMERATE = 1000;
    private Configuration configuration;
    private ArduinoConnector arduinoConnector;
    private ArrayList<Pixel> pixelArrayList = new ArrayList<>();
    private ArrayList<Color> oldColor = new ArrayList<>();

    public AmbiLoader(Configuration configuration, ArduinoConnector arduinoConnector) {
        ambitimer = new Timer(1000 / FRAMERATE, this);
        this.configuration = configuration;
        this.arduinoConnector = arduinoConnector;

        for (ScreenConfiguration screenConfiguration : configuration.getLayoutGraphicses()) {
            for (ScreenSide screenSide : screenConfiguration.getScreenSides()) {
                for (Pixel pixel : screenSide.getPixels()) {
                    pixelArrayList.add(pixel);
                    oldColor.add(pixel.getColor());
                }
            }
        }

    }

    @Override
    public void run() {

        ambitimer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        configuration.updateAmbilight();
        for (int i = 0; i < pixelArrayList.size(); i++) {
            if (pixelArrayList.get(i).getColor() != oldColor.get(i)) {
                arduinoConnector.sendPixel(pixelArrayList.get(i));
                oldColor.set(i, pixelArrayList.get(i).getColor());
            }
        }

    }
}
