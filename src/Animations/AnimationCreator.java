package Animations;

import ArduinoConnector.ArduinoConnector;
import DataStructure.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Bart on 7-5-2016.
 */
public class AnimationCreator extends JFrame {
    public static void main(String[] args){
        new AnimationCreator(AnimationManager.load(new File("AnimationManager.json")));
    }

   private AnimationManager animationManager;
    private TimeLinePanel timeLine;
    public AnimationCreator(AnimationManager animationManager){
        super();
        setVisible(true);

         /*     TESTPORPOSES */
        //animationManager.addAnimation(10,35,new Animation(new Pixel(2),Color.blue));
        //animationManager.addAnimation(20,65,new Animation(new Pixel(3),Color.YELLOW));
        setSize(1000,1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* OTHER THEN LOADING TODO SEARCH BEST WAY FOR LOADING*/
        Configuration configuration = Configuration.load();


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                configuration.save();
                animationManager.save();
            }
        });
        timeLine = new TimeLinePanel(animationManager,configuration);
        timeLine.setLayout(null);
        setContentPane(timeLine);
        repaint();
        revalidate();

    }






}

class TimeLinePanel extends JPanel implements ActionListener, MouseMotionListener{
    private AnimationManager animationManager;
    private Configuration configuration;
    private BufferedImage backgroundImage;
    ArrayList<Pixel> pixelArrayList = new ArrayList<>();
    private double currentTime,maxTime;
    private Timer timer = new Timer(1,this);
    private Animation selected = null;


    public TimeLinePanel(AnimationManager animationManager, Configuration configuration){
        this.animationManager = animationManager;
        this.configuration = configuration;
        this.currentTime = 0;
        this.maxTime = 100;

        for(ScreenConfiguration screenConfiguration : configuration.getLayoutGraphicses()){
            for(ScreenSide screenSide : screenConfiguration.getScreenSides()){
                pixelArrayList.addAll(screenSide.getPixels());

            }
        }
        pixelArrayList.sort(new PixelSortComparater());

        addMouseMotionListener(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                //super.mousePressed(mouseEvent);
                System.out.println(mouseEvent.getClickCount());
                if(mouseEvent.getClickCount() > 1){
                    System.out.println("CLICKED LOT");
                }else {
                    int stapgrootte = getHeight() / pixelArrayList.size();
                    double selectedTime = mouseEvent.getX() / (getWidth() / maxTime);
                    selected = null;

                    int positie = (int) (Math.floor(mouseEvent.getY() / stapgrootte));
                    for (Animation animation : animationManager.getTimeLine()) {
                        if (animation.getStartTime() < selectedTime && animation.getAfterTime() > selectedTime) {
                            if (search(animation.getPixel()) == positie) {
                                selected = animation;
                            }
                        }
                    }
                    if (selected == null) {
                        currentTime = selectedTime;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                mousePressed(mouseEvent);
            }
        });
        timer.start();

    }

    private int search(Pixel pixel){
        for(int i = 0; i < pixelArrayList.size(); i++){
            if(pixel.getId() == pixelArrayList.get(i).getId()){
                return i;
            }
        }
        return -9999;
    }


    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(selected != null){
            double selectedTime = mouseEvent.getX()/(getWidth()/maxTime);
            int time = (int)(Math.floor(selectedTime));
            selected.setEffectTime(time,selected.getAfterTime() - selected.getStartTime() + time);
            repaint();

        }else{
            getMouseListeners()[0].mousePressed(mouseEvent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        currentTime += .01;

        if(currentTime > maxTime){
            currentTime = 0.0;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        paintBackground();
        graphics.drawImage(backgroundImage,0,0,null);
    }



    public void paintBackground(){
        this.backgroundImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) backgroundImage.getGraphics();

        graphics2D.setFont(new Font("Arial",Font.BOLD,30));

        int x = 0;
        int y = 0;
        int stap = getHeight()/pixelArrayList.size();
        Color[] colors = { new Color(163,255,78), new Color(86,214,255)};

        for(int i = 0; i < pixelArrayList.size(); i++){
            Rectangle2D rectangle2D = new Rectangle2D.Double(x,y,getWidth(),y+stap);
            y += stap;
            graphics2D.setColor(colors[i%colors.length]);
            graphics2D.fill(rectangle2D);

            //graphics2D.setColor(colors[(i+1)%colors.length]);
            graphics2D.setColor(Color.black);
            graphics2D.drawString("Pixel " + pixelArrayList.get(i).getId(),(int)(getWidth()/2.0)-graphics2D.getFont().getSize(),y-(graphics2D.getFont().getSize()/2));
        }

        for(Animation animation : animationManager.getTimeLine()){
            double calculation = getWidth()/maxTime;
            Rectangle2D animationRectangle = new Rectangle2D.Double(calculation*animation.getStartTime(),
                                                                    (search(animation.getPixel()))*stap,
                                                                    calculation*animation.getAfterTime()-calculation*animation.getStartTime(),
                                                                    stap);
            Color color = colors[(search(animation.getPixel())%colors.length)];
            //graphics2D.setColor(color.darker());
            graphics2D.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),128));
            if(Math.floor(currentTime) >= animation.getStartTime() && Math.floor(currentTime) < animation.getAfterTime()){
                graphics2D.setColor(animation.getColor());
            }
            graphics2D.fill(animationRectangle);

            graphics2D.setColor(Color.black);
            graphics2D.draw(animationRectangle);

        }

        Rectangle2D rectangle2D = new Rectangle2D.Double((getWidth()/maxTime)*currentTime,0,10,getHeight());

        graphics2D.setColor(new Color(0,0,0,128));
        graphics2D.fill(rectangle2D);

    }

}


