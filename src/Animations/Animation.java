package Animations;

import ArduinoConnector.ArduinoConnector;
import DataStructure.Pixel;

import java.awt.*;

/**
 * Created by Bart Machielsen on 5-5-2016.
 */
public class Animation {
    private Pixel pixel;
    private Effect startEffect, afterEffect;
    private int startTime, endTime;
    private Color color;
    private Color tempColor;
    private boolean reached = false;

    public Animation(Pixel pixel, Color color) {
        this.pixel = pixel;
        this.color = color;
        tempColor = new Color(0, 0, 0);
        startEffect = Effect.FADE;
        startEffect = Effect.FADE;
    }

    public void animate(ArduinoConnector arduinoConnector, int currentTime) {
        //addColor(startEffect.getEffect(color.getRed()),startEffect.getEffect(color.getGreen()),startEffect.getEffect(color.getBlue()));


        // pixel.setColor(tempColor);
        pixel.setColor(Color.blue);
        arduinoConnector.sendPixels(pixel);
    }

    public void addColor(int red, int green, int blue) {
        red += tempColor.getRed();
        green += tempColor.getGreen();
        blue += tempColor.getBlue();
        if (red > 255) {
            red = 255;
        }
        if (red < 0) {
            red = 0;
        }

        if (green > 255) {
            green = 255;
        }
        if (green < 0) {
            green = 0;
        }

        if (blue > 255) {
            blue = 255;
        }
        if (blue < 0) {
            blue = 0;
        }

        tempColor = new Color(red, green, blue);
    }

    public void setColor(int red, int green, int blue) {
        tempColor = new Color(red, green, blue);
    }

    public void setEffectTime(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setEffects(Effect startEffect, Effect afterEffect) {
        this.startEffect = startEffect;
        this.afterEffect = afterEffect;
    }
}

