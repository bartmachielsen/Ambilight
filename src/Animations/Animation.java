package Animations;

import DataStructure.Pixel;

import java.awt.*;

/**
 * Created by Bart Machielsen on 5-5-2016.
 */
public class Animation {
    private Pixel pixel;
    private Effect startEffect, afterEffect;
    private int startTime, afterTime;
    private double afterDoubleTime;
    private Color color;
    private Color tempColor;


    public Animation(Pixel pixel, Color color) {
        this.pixel = pixel;
        this.color = color;
        tempColor = new Color(0, 0, 0);
        startEffect = Effect.FADE;
        afterEffect = Effect.FADE;


    }

    public void animate(int currentTime) {
        if (currentTime < afterDoubleTime) {
            if (compare(tempColor, color)) {
                addColor(startEffect.getEffect(color.getRed()), startEffect.getEffect(color.getGreen()), startEffect.getEffect(color.getBlue()));
            }
        } else {
            if (!compare(tempColor, Color.black)) {
                addColor(-afterEffect.getEffect(color.getRed()), -afterEffect.getEffect(color.getGreen()), -afterEffect.getEffect(color.getBlue()));
            }
        }
        pixel.setColor(tempColor);


    }


    private void addColor(int red, int green, int blue) {
        red += tempColor.getRed();
        green += tempColor.getGreen();
        blue += tempColor.getBlue();
        if (red > color.getRed()) red = color.getRed();
        if (red < 0) red = 0;
        if (green > color.getGreen()) green = color.getGreen();
        if (green < 0) green = 0;
        if (blue > color.getBlue()) blue = color.getBlue();
        if (blue < 0) blue = 0;
        tempColor = new Color(red, green, blue);
    }


    public void setEffectTime(int startTime, int afterTime) {
        this.startTime = startTime;
        this.afterTime = afterTime;

        this.afterDoubleTime = (int) (afterTime - (1 / afterEffect.getAfbouwing()));
    }

    public void setEffects(Effect startEffect, Effect afterEffect) {
        this.startEffect = startEffect;
        this.afterEffect = afterEffect;
    }

    private boolean compare(Color color, Color color1) {
        return color.getRGB() < color1.getRGB();
    }

    public Pixel getPixel() {
        return pixel;
    }


    public int getStartTime() {
        return startTime;
    }

    public int getAfterTime() {
        return afterTime;
    }

    public Color getColor() {
        return color;
    }

}

