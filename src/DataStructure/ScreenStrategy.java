package DataStructure;

/**
 * Created by Bart Machielsen on 5-5-2016.
 */
public abstract class ScreenStrategy {

    private PixelLocation pixelLocation;


    public ScreenStrategy() {

    }

    public void getPixelArea(Pixel pixel) {

    }

    private enum PixelLocation {
        OWN(),
        FIXED(),
        SCREEN();

        PixelLocation() {

        }
    }

}
