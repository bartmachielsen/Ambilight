package NeopixelLayout;

import javax.swing.*;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class PixelSettings extends JFrame {
    public PixelSettings(LayoutPanel layoutPanel, Pixel pixel) {
        super(pixel.getId() + "");
        setVisible(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 500);
    }

}
