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
import java.util.HashMap;


/**
 * Created by Bart Machielsen on 1-5-2016.
 */
public class AmbiLoader extends Thread implements ActionListener {
    private Timer ambitimer;
    private double FRAMERATE = 120.0;
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
        ArrayList<Pixel> used = new ArrayList<>();
        for (int i = 0; i < pixelArrayList.size(); i++) {
            if (!(compareColors(pixelArrayList.get(i).getColor(), oldColor.get(i)))) {
                used.add(pixelArrayList.get(i));
                oldColor.set(i, pixelArrayList.get(i).getColor());
            }
        }
        HashMap<Color, ArrayList<Pixel>> colorFixed = new HashMap<>();
        for (Pixel pixel : used) {
            if (colorFixed.containsKey(pixel.getColor())) {
                colorFixed.get(pixel.getColor()).add(pixel);
            } else {
                colorFixed.put(pixel.getColor(), new ArrayList<>());
                colorFixed.get(pixel.getColor()).add(pixel);
            }
        }
        for (ArrayList<Pixel> pixels : colorFixed.values()) {
            Pixel[] pixels1 = new Pixel[pixels.size()];
            arduinoConnector.sendPixels(pixels.toArray(pixels1));
        }

    }

    public void stopThread() {
        ambitimer.stop();
    }

    public void restart() {
        ambitimer.start();
    }

    public void setColor(Color color) {
        Pixel[] pixels = new Pixel[pixelArrayList.size()];
        for (Pixel pixel : pixelArrayList) {
            pixel.setColor(color);
        }
        arduinoConnector.sendPixels(pixelArrayList.toArray(pixels));
        //loop();
    }

    public void loop() {
        for (Pixel pixel : pixelArrayList) {
            pixel.setColor(new Color(255, 0, 0));
            arduinoConnector.sendPixels(pixel);
            System.out.println(pixel.getId());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean compareColors(Color color1, Color color2) {
        int verschil = color1.getRGB() - color2.getRGB();
        return !(verschil > 10 || verschil < 10);
    }

}
