package NeopixelLayout;

import DataStructure.Pixel;
import DataStructure.ScreenConfiguration;
import DataStructure.ScreenSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class ScreenPanel extends JPanel {
    private int width, height;
    private int[] pixelSize = {5, 5, 5, 5};
    private Pixel selectedPixel = null;
    private boolean once = true;
    private boolean showArea = false;
    private ScreenConfiguration screenConfiguration;

    public ScreenPanel(ScreenConfiguration screenConfiguration) {
        super();
        this.screenConfiguration = screenConfiguration;
        this.width = screenConfiguration.getGraphicsDevice().getDisplayMode().getWidth();
        this.height = screenConfiguration.getGraphicsDevice().getDisplayMode().getHeight();



        revalidate();
        repaint();


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (ScreenSide screenSide : screenConfiguration.getScreenSides()) {
                    for (int ii = 0; ii < screenSide.getPixels().size(); ii++) {
                        Pixel pixel = screenSide.getPixels().get(ii);
                        int x = pixel.berekenLocatieX(getWidth());
                        int y = pixel.berekenLocatieY(getHeight());
                        Rectangle2D rectangle2D = new Rectangle2D.Double(x - 15, y - 15, 30, 30);

                        if (rectangle2D.contains(e.getX(), e.getY())) {
                            selectedPixel = pixel;
                            System.out.println(pixel.toString());
                        }


                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mouseClicked(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                selectedPixel = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPixel != null) {
                    selectedPixel.setLocation(e.getX() - 30, e.getY() - 30, getWidth() - 50, getHeight() - 50);
                    repaint();
                }
            }
        });

    }


    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (once) {
            for (int i = 0; i < screenConfiguration.getScreenSides().size(); i++) {
                screenConfiguration.getScreenSides().get(i).generatePixelsDrawing(getWidth(), getHeight());
            }
            once = false;
        }


        //new Virtualizer(sides, graphicsDevice);
        // GRAPHICS2D IS MUCH BETTER
        Graphics2D graphics2D = (Graphics2D) graphics;

        // SET BACKGROUNDCOLOR
        graphics2D.setBackground(Color.white);


        // CREATE SCREENBOX
        Rectangle2D screenDimension = new Rectangle2D.Double(25, 25, getWidth() - 50, getHeight() - 50);

        // DRAW FILLED BOX ABOVE BACKGROUNDPICTURE
        graphics2D.setColor(new Color(163, 255, 78, 100));
        graphics2D.fill(screenDimension);

        // DRAW BORDER
        graphics2D.setColor(Color.black);
        graphics2D.draw(screenDimension);

        // DRAW SCREEN INFORMATION
        graphics2D.setFont(new Font("Arial", Font.BOLD, 20));
        graphics2D.drawString("Display " + screenConfiguration.getGraphicsDevice().getIDstring().split("y")[1]
                + "  |  " + width + "x" + height, 30, 20);


        // DRAW GENERATED PIXELS
        for (ScreenSide screenSide : screenConfiguration.getScreenSides()) {
            for (Pixel pixel : screenSide.getPixels()) {

                if (showArea) {
                    int[] cords = pixel.getScreenDimension(getWidth() - 50, getHeight() - 50);
                    Area area = new Area(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
                    area.subtract(new Area(screenDimension));
                    graphics2D.setColor(new Color(163, 255, 78, 150));
                    Area area2 = new Area(new Rectangle2D.Double(cords[0] + 35, cords[1] + 3, cords[2], cords[3]));
                    area2.subtract(area);
                    graphics2D.fill(area2);

                }


                Rectangle2D rectangle2D = new Rectangle2D.Double(pixel.berekenLocatieX(getWidth() - 50) + 25, pixel.berekenLocatieY(getHeight() - 50) + 25, 20, 20);
                //graphics2D.setColor(new Color(163, 255, 78, 255));
                graphics2D.setColor(pixel.getColor());
                graphics2D.fill(rectangle2D);
                //pixel.setShape(rectangle2D);
                graphics2D.setColor(Color.black);
                graphics2D.draw(rectangle2D);

                int stringOffset = 2;
                graphics2D.setFont(new Font("Arial", Font.BOLD, 12));
                if (pixel.getId() > 9) {
                    stringOffset = -2;
                }
                graphics2D.drawString(pixel.getId() + "", pixel.berekenLocatieX(getWidth() - 50) + 30 + stringOffset, pixel.berekenLocatieY(getHeight() - 50) + 40);




            }
        }


    }

    public ArrayList<ScreenSide> getSides() {
        return screenConfiguration.getScreenSides();
    }
    public GraphicsDevice getGraphicsDevice() {
        return screenConfiguration.getGraphicsDevice();
    }

    public void generate() {
        once = true;
        repaint();
    }
    public void createPixels(){
        for (int i = 0; i < screenConfiguration.getScreenSides().size(); i++) {
            screenConfiguration.getScreenSides().get(i).setPixelSize(pixelSize[i]);
            screenConfiguration.getScreenSides().get(i).setVisible(true);
        }
    }
}
