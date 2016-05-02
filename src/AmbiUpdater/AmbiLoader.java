package AmbiUpdater;

import ArduinoConnector.ArduinoConnector;
import DataStructure.Configuration;
import DataStructure.Pixel;
import DataStructure.ScreenConfiguration;
import DataStructure.ScreenSide;

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
    private double FRAMERATE = 1000.0;
    private Configuration configuration;
    private ArduinoConnector arduinoConnector;
    private ArrayList<Pixel> pixelArrayList = new ArrayList<>();
    private ArrayList<Color> oldColor = new ArrayList<>();

    public AmbiLoader(Configuration configuration, ArduinoConnector arduinoConnector) {
        ambitimer = new Timer((int) (1000.0 / FRAMERATE), this);
        this.configuration = configuration;
        this.arduinoConnector = arduinoConnector;

        for (ScreenConfiguration screenConfiguration : configuration.getLayoutGraphicses()) {
            for (ScreenSide screenSide : screenConfiguration.getScreenSides()) {
                for (Pixel pixel : screenSide.getPixels()) {
                    pixelArrayList.add(pixel);
                    oldColor.add(new Color(0, 0, 0));
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
        ArrayList<Pixel> unused = new ArrayList<>();
        for (int i = 0; i < pixelArrayList.size(); i++) {
            if (!(pixelArrayList.get(i).getColor().getRGB() == oldColor.get(i).getRGB())) {
                arduinoConnector.sendPixel(pixelArrayList.get(i));
                oldColor.set(i, pixelArrayList.get(i).getColor());
            } else {
                unused.add(pixelArrayList.get(i));
            }
        }

    }

    public void stopThread() {
        ambitimer.stop();
    }

    public void restart() {
        ambitimer.start();
    }

    public void setColor(Color color) {
        for (Pixel pixel : pixelArrayList) {
            pixel.setColor(color);
            arduinoConnector.sendPixel(pixel);
        }
    }

}
