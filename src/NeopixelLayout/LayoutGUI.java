package NeopixelLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

/**
 * Created by Bart on 28-4-2016.
 */
public class LayoutGUI extends JFrame {
    private GraphicsDevice[] graphicsDevices = null;

    public LayoutGUI(){
        // INITIALIZING JFRAME, SETTING VISIBLE AND LAYOUT
        super("PixelLayout Manager");
        setVisible(true);
        setSize(1500,600);
        setMinimumSize(new Dimension(1000,600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);
        // SET WINDOWS STYLED
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            e.printStackTrace();
        }


        // GETTING SCREEN AMOUNT
        graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        // CREATE LAYOUT MANAGER
        SpringLayout springLayout = new SpringLayout();


        // SETTING CONTENTPANE AND CREATE SCREENPANES
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(springLayout);

        JPanel settingsPanel = new JPanel();
        contentPane.add(settingsPanel);

        JPanel screenPanel = new JPanel();
        contentPane.add(screenPanel);




        // SPRINGLAYOUT SETTINGS AND CONFIGURATION
        springLayout.putConstraint(SpringLayout.NORTH,screenPanel,200,SpringLayout.NORTH,contentPane);
        springLayout.putConstraint(SpringLayout.WEST,screenPanel,0,SpringLayout.WEST,contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH,screenPanel,0,SpringLayout.SOUTH,contentPane);
        springLayout.putConstraint(SpringLayout.EAST,screenPanel,0,SpringLayout.EAST,contentPane);

        springLayout.putConstraint(SpringLayout.NORTH,settingsPanel,0,SpringLayout.NORTH,contentPane);
        springLayout.putConstraint(SpringLayout.WEST,settingsPanel,0,SpringLayout.WEST,contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH,settingsPanel,0,SpringLayout.NORTH,screenPanel);
        springLayout.putConstraint(SpringLayout.EAST,settingsPanel,0,SpringLayout.EAST,contentPane);

        // SETTINGSPANEL
        settingsPanel.setLayout(new GridLayout(5,1));
        JLabel titleLabel = new JLabel("  PIXELSETTINGS");
        titleLabel.setFont(new Font("Helvetica" ,Font.BOLD,35));


        settingsPanel.add(titleLabel);




        //generating layout                                                                     TODO generate layout instead of fixed
        screenPanel.setLayout(new GridLayout(1,graphicsDevices.length));
        LayoutPanel[] layoutPanels = new LayoutPanel[graphicsDevices.length];
        for(int i = 0; i < graphicsDevices.length; i++){
            layoutPanels[i] =  new LayoutPanel(graphicsDevices[i]);
            screenPanel.add(layoutPanels[i]);
        }



        revalidate();
    }

}

class LayoutPanel extends JPanel{
    private GraphicsDevice graphicsDevice;
    private int width,height;
    private Shape[] motionSpaces = new Shape[4];
    private Shape hovershape = null;
    private Pixel[][] pixels = new Pixel[4][];

    public LayoutPanel(GraphicsDevice graphicsDevice) {
        super();
        this.width = graphicsDevice.getDisplayMode().getWidth();
        this.height = graphicsDevice.getDisplayMode().getHeight();
        this.graphicsDevice = graphicsDevice;








        revalidate();
        repaint();
    }


    public void generatePixels(int numpixelsX, int numpixelsY){
        int x = 0;
        int y = 0;

        for(int ii = 0; ii < 4; ii++) {                             // USE OF ENUM      TODO CHECK IF ENUM IS BETTER
            int numpixels = numpixelsX;

            if( ii % 2 != 0){
                    numpixels = numpixelsY;
            }
            pixels[ii] = new Pixel[numpixels];
            for (int i = 0; i < numpixels; i++) {
               if( ii % 2 != 0){
                   pixels[ii][i] = new Pixel(new Point(getWidth()/numpixels,50));
               }else{
                   pixels[ii][i] = new Pixel(new Point(50, getHeight()/numpixels));
               }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        generatePixels(5,5);

        // GRAPHICS2D IS MUCH BETTER
        Graphics2D graphics2D = (Graphics2D) graphics;

        // SET BACKGROUNDCOLOR
        graphics2D.setBackground(Color.white);





        // CREATE SCREENBOX
        Rectangle2D screenDimension = new Rectangle2D.Double(25,25,getWidth()-50,getHeight()-50);

        // DRAW FILLED BOX ABOVE BACKGROUNDPICTURE                          TODO BETTER COLOR
        graphics2D.setColor(new Color(163,255, 78,128));
        graphics2D.fill(screenDimension);

        // DRAW BORDER
        graphics2D.setColor(Color.black);
        graphics2D.draw(screenDimension);

        // DRAW SCREEN INFORMATION
        graphics2D.setFont(new Font("Arial",Font.BOLD,20));
        graphics2D.drawString("Display " + graphicsDevice.getIDstring().split("y")[1]
                                        + "  |  " + width + "x" + height,30,20);

        for(Pixel[] pixels1 : pixels){
            for(Pixel pixel : pixels1){
                Rectangle2D rectangle2D = new Rectangle2D.Double(pixel.getLocation().getX(),pixel.getLocation().getY(),20,20);
                graphics2D.fill(rectangle2D);
            }
        }



    }
}

class PixelSettings {
    public static Pixel[] pixelSetter(Pixel[] pixels, Shape shape, GraphicsDevice graphicsDevice){
        Object[] options = {"Opslaan", "Genereren", "Terug" };


        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(10,1));
        contentPane.add(new JLabel("AUTO CONFIGURATION"));
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(pixels.length,0,100,1);
        JSpinner jSpinner = new JSpinner(spinnerNumberModel);
        contentPane.add(jSpinner);






        int keuze = JOptionPane.showOptionDialog(contentPane,contentPane,"pixelSettings",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);

        int value = (int)jSpinner.getValue();
        if(value != pixels.length){
            Pixel[] replacement = new Pixel[value];
            if(shape.getBounds().getWidth() > shape.getBounds().getHeight()){
                for(int i = 0; i < value; i++){
                    replacement[i] = new Pixel(new Point((int)(((shape.getBounds().getWidth()-50)/value)+50),50));
                }
            }else{
                for(int i = 0; i < value; i++){
                    replacement[i] = new Pixel(new Point(50,(int)(((shape.getBounds().getHeight()-50)/value)+50)));
                }
            }
            pixels = replacement;
        }

        return pixels;


    }
}