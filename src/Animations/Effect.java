package Animations;

/**
 * Created by Bart Machielsen on 5-5-2016.
 */
public enum Effect {
    FADE(0.1),
    INSTANT(1.0);


    double afbouwing;

    Effect(double afbouwing) {
        this.afbouwing = afbouwing;
    }

    public int getEffect(int waarde) {
        waarde = (int) (waarde * afbouwing);
        if (waarde > 255) {
            return 255;
        }
        if (waarde < 0) {
            return 0;
        }
        return waarde;
    }
}
