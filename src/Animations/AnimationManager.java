package Animations;

import ArduinoConnector.ArduinoConnector;
import DataStructure.Configuration;
import DataStructure.Pixel;
import DataStructure.ScreenConfiguration;
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

/**
 * Created by Bart Machielsen on 5-5-2016.
 */
public class AnimationManager implements ActionListener {
    private File file = new File("AnimationManager.json");
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


    public static AnimationManager load(File file){
        try{
            Gson gson = new Gson();
            JsonReader jsonReader = gson.newJsonReader(new FileReader(file));
            AnimationManager animationManager = gson.fromJson(jsonReader,AnimationManager.class);
            return animationManager;
        }catch (Exception e){
            return null;
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
}
