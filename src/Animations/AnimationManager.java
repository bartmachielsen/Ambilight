package Animations;

import ArduinoConnector.ArduinoConnector;
import DataStructure.Pixel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        timeLineTimer = new Timer(1000, this);


    }

    public static void main(String[] args) {
        Pixel pixel = new Pixel();
        pixel.setId(1);
        Animation animation = new Animation(pixel, new Color(10, 100, 200));
        AnimationManager animationManager = new AnimationManager(100, new ArduinoConnector());
        animationManager.addAnimation(10, 60, animation);

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
            animation.animate(arduinoConnector, currentTime);
        }
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
}
