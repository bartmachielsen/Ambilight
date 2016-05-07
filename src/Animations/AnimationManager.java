package Animations;

import ArduinoConnector.ArduinoConnector;
import DataStructure.Pixel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bart Machielsen on 5-5-2016.
 */
public class AnimationManager implements ActionListener {
    private ArrayList<Animation> timeLine;
    private ArduinoConnector arduinoConnector;
    private Timer timeLineTimer;
    private int currentTime = 0;
    private int totalTime;

    public AnimationManager(int time, ArduinoConnector arduinoConnector) {
        this.totalTime = time;
        timeLine = new ArrayList<>();
        this.arduinoConnector = arduinoConnector;
        timeLineTimer = new Timer(0, this);


    }

    public static void main(String[] args) {

        AnimationManager animationManager = new AnimationManager(16, new ArduinoConnector());

        for (int i = 1; i < 75; i++) {
            Pixel pixel = new Pixel();
            pixel.setId(i);
            Animation animation = new Animation(pixel, new Color(0, 0, 255));
            animationManager.addAnimation(10, 15, animation);
        }

        animationManager.start();


    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentTime > totalTime)
            currentTime = 0;
        for (Animation animation : timeLine) {
            animation.animate(currentTime);

        }
        toArduinoStrip();
        currentTime++;
    }

    public void addAnimation(int startTime, int endTime, Animation animation) {
        animation.setEffectTime(startTime, endTime);
        timeLine.add(animation);
    }





    /*         TIMERCONTROL         */

    public void start() {
        timeLineTimer.start();
    }

    public void stop() {
        timeLineTimer.stop();
    }

    public void toArduinoStrip() {
        HashMap<Color, ArrayList<Pixel>> colors = new HashMap<>();
        for (Animation animation : timeLine) {
            ArrayList<Pixel> pixelArrayList = colors.get(animation.getPixel().getColor());
            if (pixelArrayList == null) {
                pixelArrayList = new ArrayList<>();
                colors.put(animation.getPixel().getColor(), pixelArrayList);
            }
            colors.get(animation.getPixel().getColor()).add(animation.getPixel());
        }

        for (ArrayList<Pixel> pixels : colors.values()) {
            Pixel[] pixelArray = new Pixel[pixels.size()];
            arduinoConnector.sendPixels(pixels.toArray(pixelArray));
        }




    }
}
