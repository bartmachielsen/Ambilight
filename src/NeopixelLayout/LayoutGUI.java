package NeopixelLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Bart on 28-4-2016.
 */
public class LayoutGUI extends JFrame {
    private GraphicsDevice[] graphicsDevices = null;
    public LayoutGUI(){
        // INITIALIZING JFRAME, SETTING VISIBLE AND LAYOUT
        super("Layout Manager");
        setVisible(true);
        setSize(1000,1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        // GETTING SCREEN AMOUNT
        graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        // SETTING CONTENTPANE AND CREATE SCREENPANES

        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        //generating layout                                                                     TODO generate layout instead of fixed


        contentPane.setLayout(new GridLayout(1,graphicsDevices.length));

        for(int i = 0; i < graphicsDevices.length; i++){
            contentPane.add(new LayoutPanel(graphicsDevices[i]));
        }

        revalidate();
    }

}

class LayoutPanel extends JPanel{
    private GraphicsDevice graphicsDevice;
    private BufferedImage backgroundImage;
    private int width,height;
    private Shape[] motionSpaces = new Shape[4];
    private Shape hovershape = null;
    public LayoutPanel(GraphicsDevice graphicsDevice) {
        super();
        this.width = graphicsDevice.getDisplayMode().getWidth();
        this.height = graphicsDevice.getDisplayMode().getHeight();
        this.graphicsDevice = graphicsDevice;

        // GET SCREENCAPTURE
        try {
            backgroundImage = new Robot(graphicsDevice).createScreenCapture(new Rectangle(width, height));
        } catch (Exception e){
            e.printStackTrace();
        }





        // ADD LISTENERS FOR MOTION AND CLICK EVENTS
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                super.mouseMoved(mouseEvent);
                hovershape = null;
                for(Shape shape : motionSpaces){
                    if(shape != null) {
                        if (shape.contains(mouseEvent.getX(), mouseEvent.getY())) {
                            hovershape = shape;
                            System.out.println(shape.toString());
                            repaint();
                            break;
                        }
                    }
                }


            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);

                System.out.println("MOUSECLICKED");
            }
        });

        revalidate();
        repaint();
    }

    public void generateMotionSpace(){
        motionSpaces[0] = new Rectangle2D.Double(25,25,getWidth()-50,(getHeight()-50)/4);
        motionSpaces[1] = new Rectangle2D.Double(25,25,(getWidth()-50)/4,getHeight()-50);
        motionSpaces[2] = new Rectangle2D.Double(getWidth()-25-(getWidth()-50)/4,25,(getWidth()-50)/4,getHeight()-50);
        motionSpaces[3] = new Rectangle2D.Double(25,(getHeight()/4)*3,getWidth()-50,(getHeight()-50)/4);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // GENERATE SPACE  -- WHY HERE? BECAUSE OTHERWISE NOT THE RIGHT DIMENSION
        generateMotionSpace();


        Graphics2D graphics2D = (Graphics2D) graphics;

        // SET BACKGROUNDCOLOR
        graphics2D.setBackground(Color.white);


        // RESIZE AND DRAW BACKGROUND IMAGE
        BufferedImage resized = new BufferedImage(getWidth()-50, getHeight()-50, backgroundImage.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(backgroundImage, 0, 0, getWidth()-50, getHeight()-50, 0, 0, backgroundImage.getWidth(),
                backgroundImage.getHeight(), null);
        g.dispose();
        graphics2D.drawImage(resized,25,25,null);



        // CREATE SCREENBOX
        Rectangle2D screenDimension = new Rectangle2D.Double(25,25,getWidth()-50,getHeight()-50);

        // DRAW FILLED BOX ABOVE BACKGROUNDPICTURE      TODO BETTER COLOR
        graphics2D.setColor(new Color(163,255, 78,128));
        graphics2D.fill(screenDimension);

        // DRAW BORDER
        graphics2D.setColor(Color.black);
        graphics2D.draw(screenDimension);

        // DRAW SCREEN INFORMATION
        graphics2D.setFont(new Font("Arial",Font.BOLD,20));
        graphics2D.drawString(width + "x" + height,30,60);
        graphics2D.drawString("Screen " + graphicsDevice.getIDstring().split("y")[1], 30, 80);


        // DRAW HOVERSHAPE
        if(hovershape != null){
            graphics2D.setColor(new Color(163,255, 78,255));            // TODO BETTER COLOR ALSO
            graphics2D.fill(hovershape);
        }


    }
}

