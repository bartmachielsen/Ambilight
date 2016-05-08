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
    private boolean remove = false;
    private boolean changed = false;


    public Animation(Pixel pixel, Color color) {
        this.pixel = pixel;
        this.color = color;
        tempColor = new Color(0, 0, 0);
        startEffect = Effect.FADE;
        afterEffect = Effect.FADE;


    }


    public void animate(int currentTime) {
        if (remove) return;
        changed = false;
        if (currentTime >= startTime) {
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
        } else {
            if (pixel.getColor() != Color.black) {
                pixel.setColor(Color.black);
                changed = true;
            }
        }

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
        changed = true;
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

    public void setPixel(Pixel pixel) {
        this.pixel = pixel;
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

    public void setColor(Color color) {
        this.color = color;
    }

    public Effect getAfterEffect() {
        return afterEffect;
    }

    public void setAfterEffect(Effect afterEffect) {
        this.afterEffect = afterEffect;
    }

    public Effect getStartEffect() {
        return startEffect;
    }

    public void setStartEffect(Effect startEffect) {
        this.startEffect = startEffect;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
        if (remove) {
            changed = true;
            pixel.setColor(Color.black);
        }
    }


    public Animation getInstance() {
        Animation animation = new Animation(pixel, color);
        animation.setEffects(startEffect, afterEffect);
        animation.setEffectTime(startTime, afterTime);
        return animation;
    }

    public boolean isChanged() {
        return changed;
    }
}

