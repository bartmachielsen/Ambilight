package NeopixelLayout;

import DataStructure.Configuration;
import DataStructure.Pixel;
import DataStructure.ScreenConfiguration;
import DataStructure.ScreenSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class Virtualizer {

    ArrayList<JFrame> frames = new ArrayList<>();
    boolean visible = false;

    public Virtualizer(Configuration configuration) {
        for (ScreenConfiguration screenConfiguration : configuration.getLayoutGraphicses()) {
            GraphicsDevice graphicsDevice = screenConfiguration.getGraphicsDevice();
            ArrayList<ScreenSide> screenSides = screenConfiguration.getScreenSides();


            GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();

            for (ScreenSide screenSide : screenSides) {
                for (Pixel pixel : screenSide.getPixels()) {
                    JFrame jFrame2 = new JFrame(graphicsConfiguration);
                    jFrame2.setType(javax.swing.JFrame.Type.UTILITY);
                    jFrame2.setUndecorated(true);
                    JPanel content = new JPanel();
                    jFrame2.setContentPane(content);
                    jFrame2.setVisible(false);
                    jFrame2.setAutoRequestFocus(true);
                    int[] dimension = pixel.getScreenDimension((int) graphicsConfiguration.getBounds().getWidth(), (int) graphicsConfiguration.getBounds().getHeight());
                    jFrame2.setSize(dimension[2], dimension[3]);
                    content.setBackground(Color.blue);
                    frames.add(jFrame2);

                    content.addMouseListener(new MouseAdapter() {
                        /**
                         * {@inheritDoc}
                         *
                         * @param e
                         */
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            System.out.println("CLICKED");

                            Color color = JColorChooser.showDialog(null, "Select color", Color.blue);
                            if (color == null) return;
                            for (JFrame frame : frames) {
                                frame.getContentPane().setBackground(color);
                            }
                        }
                    });


                    jFrame2.setLocation(graphicsConfiguration.getBounds().x + dimension[0],
                            graphicsConfiguration.getBounds().y + dimension[1]);


                }
            }
        }
    }

    public void exit() {
        for (JFrame frame : frames) {
            frame.dispose();
            frame.setVisible(false);
            visible = false;
        }
    }

    public void show() {
        for(JFrame frame : frames){
            frame.setVisible(true);
            visible = true;
        }
    }

    public boolean isVisible() {
        return visible;
    }
}
