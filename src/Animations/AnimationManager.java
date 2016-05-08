package Animations;

import ArduinoConnector.ArduinoConnector;
import DataStructure.Pixel;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Bart Machielsen on 5-5-2016.
 */
public class AnimationManager implements ActionListener {
    private File file = new File("AnimationManager.json");
    private ArrayList<Animation> timeLine;
    private transient ArduinoConnector arduinoConnector;
    private Timer timeLineTimer;
    private double currentTime = 0.0;
    private int totalTime;

    public AnimationManager(int time, ArduinoConnector arduinoConnector) {
        this.totalTime = time;
        this.arduinoConnector = arduinoConnector;

        timeLine = new ArrayList<>();
        timeLineTimer = new Timer(100, this);


    }

    public static void main(String[] args) {

        AnimationManager animationManager = AnimationManager.load(new File("AnimationManager.json"));
        animationManager.reload();
        animationManager.setArduinoConnector(new ArduinoConnector());
        animationManager.start();


    }

    public static AnimationManager load(File file) {
        try {
            Gson gson = new Gson();
            JsonReader jsonReader = gson.newJsonReader(new FileReader(file));
            AnimationManager animationManager = gson.fromJson(jsonReader, AnimationManager.class);
            return animationManager;
        } catch (Exception e) {
            return null;
        }
    }

    public void reload() {
        timeLineTimer = new Timer(100, this);
    }

    public void setArduinoConnector(ArduinoConnector arduinoConnector) {
        this.arduinoConnector = arduinoConnector;

    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        for (Animation animation : timeLine) {
            animation.animate((int) currentTime);

        }
        toArduinoStrip();
        currentTime += 1;
        if (currentTime > totalTime) {
            currentTime = 0.0;
        }
    }





    /*         TIMERCONTROL         */

    public void addAnimation(int startTime, int endTime, Animation animation) {
        animation.setEffectTime(startTime, endTime);
        timeLine.add(animation);
    }

    public void start() {
        timeLineTimer.start();
    }

    public void stop() {
        timeLineTimer.stop();
    }

    public void toArduinoStrip() {


        HashMap<Color, ArrayList<Pixel>> colors = new HashMap<>();
        for (Animation animation : timeLine) {
            if (animation.isChanged()) {
                ArrayList<Pixel> pixelArrayList = colors.get(animation.getPixel().getColor());
                if (pixelArrayList == null) {
                    pixelArrayList = new ArrayList<>();
                    colors.put(animation.getPixel().getColor(), pixelArrayList);
                }
                colors.get(animation.getPixel().getColor()).add(animation.getPixel());
            }
        }

        for (ArrayList<Pixel> pixels : colors.values()) {
            Pixel[] pixelArray = new Pixel[pixels.size()];
            arduinoConnector.sendPixels(pixels.toArray(pixelArray));
        }

        Iterator<Animation> pixelIterator = timeLine.iterator();
        while (pixelIterator.hasNext()) {
            if (pixelIterator.next().isRemove()) {
                pixelIterator.remove();

            }
        }


    }

    public void save(File file){
        try{
            Gson gson = new Gson();
            String json = gson.toJson(this);

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void save(){
        save(file);
    }

    public ArrayList<Animation> getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(ArrayList<Animation> timeLine) {
        this.timeLine = timeLine;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isRunning() {
        if (timeLineTimer == null) {
            return false;
        } else {
            return timeLineTimer.isRunning();
        }
    }

    public void speedUp() {
        int delay = timeLineTimer.getDelay() - 10;
        if (delay < 0) delay = 100;
        System.out.println(delay);
        timeLineTimer.setDelay(delay);

    }

}
