package NeopixelLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by Bart Machielsen on 29-4-2016.
 */
public class LayoutPanel extends JPanel {
    private GraphicsDevice graphicsDevice;
    private int width, height;
    private int[] pixelSize = {5, 5, 5, 5};
    private ArrayList<Side> sides;


    public LayoutPanel(GraphicsDevice graphicsDevice, ArrayList<Side> sides) {
        super();
        this.width = graphicsDevice.getDisplayMode().getWidth();
        this.height = graphicsDevice.getDisplayMode().getHeight();
        this.graphicsDevice = graphicsDevice;
        this.sides = sides;

        for (int i = 0; i < sides.size(); i++)
            sides.get(i).generatePixelsDrawing(getWidth(), getHeight(), pixelSize[i]);

        revalidate();
        repaint();


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (Side side : sides) {
                    for (int ii = 0; ii < side.getPixels().size(); ii++) {
                        if (side.getPixels().get(ii).getShape().contains(e.getX(), e.getY())) {
                            new PixelSettings(LayoutPanel.this, side.getPixels().get(ii));
                        }
                    }
                }

            }
        });

    }


    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        for (int i = 0; i < sides.size(); i++) {
            sides.get(i).generatePixelsDrawing(getWidth(), getHeight(), pixelSize[i]);
        }


        new Virtualizer(sides, graphicsDevice);
        // GRAPHICS2D IS MUCH BETTER
        Graphics2D graphics2D = (Graphics2D) graphics;

        // SET BACKGROUNDCOLOR
        graphics2D.setBackground(Color.white);


        // CREATE SCREENBOX
        Rectangle2D screenDimension = new Rectangle2D.Double(25, 25, getWidth() - 50, getHeight() - 50);

        // DRAW FILLED BOX ABOVE BACKGROUNDPICTURE
        graphics2D.setColor(new Color(163, 255, 78, 128));
        graphics2D.fill(screenDimension);

        // DRAW BORDER
        graphics2D.setColor(Color.black);
        graphics2D.draw(screenDimension);

        // DRAW SCREEN INFORMATION
        graphics2D.setFont(new Font("Arial", Font.BOLD, 20));
        graphics2D.drawString("Display " + graphicsDevice.getIDstring().split("y")[1]
                + "  |  " + width + "x" + height, 30, 20);


        // DRAW GENERATED PIXELS
        for (Side side : sides) {
            for (Pixel pixel : side.getPixels()) {
                Rectangle2D rectangle2D = new Rectangle2D.Double(pixel.getLocation().getX(), pixel.getLocation().getY(), 20, 20);
                graphics2D.setColor(new Color(163, 255, 78, 255));
                graphics2D.fill(rectangle2D);
                pixel.setShape(rectangle2D);
                graphics2D.setColor(Color.black);
                graphics2D.draw(rectangle2D);

                int stringOffset = 2;
                graphics2D.setFont(new Font("Arial", Font.BOLD, 12));
                if (pixel.getId() > 9) {
                    stringOffset = -2;
                }
                graphics2D.drawString(pixel.getId() + "", (int) (pixel.getLocation().getX() + 5) + stringOffset, (int) (pixel.getLocation().getY() + 15));

            }
        }


    }

    public ArrayList<Side> getSides() {
        return sides;
    }
}
