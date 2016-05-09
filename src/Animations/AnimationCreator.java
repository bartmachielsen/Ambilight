package Animations;

import ArduinoConnector.ArduinoConnector;
import DataStructure.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Bart on 7-5-2016.
 */
public class AnimationCreator extends JFrame {
    private TimeLinePanel timeLine;
    private boolean running, created;

    public AnimationCreator(AnimationManager animationManager, boolean creation) {
        super();
        setVisible(true);
        if (animationManager == null) {
            animationManager = new AnimationManager(100, null);
        }

        if (!creation) {
            animationManager.reload();
        }

        if (animationManager.getName().equals("new AnimationManager")) {
            animationManager.setName(JOptionPane.showInputDialog(null, "Name Scheme:", "New AnimationManager", JOptionPane.QUESTION_MESSAGE));
        }




        running = false;
        created = creation;

         /*     TESTPORPOSES */
        //animationManager.addAnimation(10,35,new Animation(new Pixel(2),Color.blue));
        //animationManager.addAnimation(20,65,new Animation(new Pixel(3),Color.YELLOW));
        setSize(1000,1000);
        setLocationRelativeTo(null);
        if (!creation) {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        } else {
            animationManager.stop();
            setDefaultCloseOperation(HIDE_ON_CLOSE);
        }

        /* OTHER THEN LOADING TODO SEARCH BEST WAY FOR LOADING*/
        Configuration configuration = Configuration.load();
        final AnimationManager animationManager1 = animationManager;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                //animationManager1.stop(); // TODO EXTRA SAFETY
                configuration.save();
                animationManager1.save();
            }
        });


        SpringLayout springLayout = new SpringLayout();
        JPanel contentPane = new JPanel(springLayout);
        setContentPane(contentPane);

        JPanel controlPanel = new JPanel(new GridLayout(1, 8));
        JButton addAll = new JButton("Animate All");
        controlPanel.add(addAll);
        addAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Animation animation = new Animation(configuration.getLayoutGraphicses().get(0).getScreenSides().get(0).getPixels().get(0), Color.white);
                AnimationForm.editAnimation(animation);
                for (ScreenConfiguration screenConfiguration : configuration.getLayoutGraphicses()) {
                    for (ScreenSide screenSide : screenConfiguration.getScreenSides()) {
                        for (Pixel pixel : screenSide.getPixels()) {
                            Animation animation1 = animation.getInstance();
                            animation1.setPixel(pixel);
                            animationManager1.addAnimation(animation.getStartTime(), animation.getAfterTime(), animation1);

                        }
                    }
                }

            }
        });
        contentPane.add(controlPanel);


        JButton moveRandom = new JButton("Place Random");
        moveRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Animation animation : animationManager1.getTimeLine()) {
                    int start = (int) (Math.random() * animationManager1.getTotalTime());

                    animation.setEffectTime(start, (animation.getAfterTime() - animation.getStartTime() + start));
                }
            }
        });

        controlPanel.add(moveRandom);

        JButton randomColor = new JButton("Random color");
        controlPanel.add(randomColor);
        randomColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Animation animation : animationManager1.getTimeLine()) {
                    animation.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                }
            }
        });

        JButton randomSize = new JButton("Random Size");
        controlPanel.add(randomSize);
        randomSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Animation animation : animationManager1.getTimeLine()) {
                    int start = animation.getStartTime();
                    int eind = 10000;
                    int trys = 0;
                    while (eind > animationManager1.getTotalTime()) {
                        eind = (int) (start + Math.random() * animationManager1.getTotalTime());
                        trys++;
                        if (trys > 10) {
                            start -= 100;
                            if (start < 0) {
                                start = 0;
                            }
                            trys = 0;
                        }
                    }
                    animation.setEffectTime(start, eind);
                }
            }
        });


        JButton deleteAll = new JButton("Remove All");
        deleteAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Animation animation : animationManager1.getTimeLine()) {
                    animation.setRemove(true);
                }
            }
        });
        controlPanel.add(deleteAll);


        JPanel arduinoControl = new JPanel(new GridLayout(3, 1));
        JButton pause = new JButton("Pause");
        JButton play = new JButton("Play");
        JButton speed = new JButton("Speed");
        arduinoControl.add(pause);
        arduinoControl.add(play);
        arduinoControl.add(speed);
        pause.setEnabled(false);
        play.setEnabled(false);
        speed.setEnabled(false);


        JButton toArduino = new JButton("ToArduino");
        controlPanel.add(toArduino);
        toArduino.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!created) {
                    animationManager1.setArduinoConnector(new ArduinoConnector());
                    created = true;

                }
                pause.setEnabled(true);
                play.setEnabled(true);
                speed.setEnabled(true);


            }
        });


        controlPanel.add(arduinoControl);


        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationManager1.stop();
            }
        });

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationManager1.start();
            }
        });

        speed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationManager1.speedUp();
            }
        });

        ArrayList<ScreenSide> screenSides = new ArrayList<>();
        for (ScreenConfiguration screenConfiguration : configuration.getLayoutGraphicses()) {
            screenSides.addAll(screenConfiguration.getScreenSides());

        }
        timeLine = new TimeLinePanel(animationManager, screenSides);
        contentPane.add(timeLine);


        springLayout.putConstraint(SpringLayout.NORTH, controlPanel, 0, SpringLayout.NORTH, contentPane);
        springLayout.putConstraint(SpringLayout.EAST, controlPanel, 0, SpringLayout.EAST, contentPane);
        springLayout.putConstraint(SpringLayout.WEST, controlPanel, 0, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, controlPanel, 50, SpringLayout.NORTH, contentPane);

        springLayout.putConstraint(SpringLayout.NORTH, timeLine, 0, SpringLayout.SOUTH, controlPanel);
        springLayout.putConstraint(SpringLayout.EAST, timeLine, 0, SpringLayout.EAST, contentPane);
        springLayout.putConstraint(SpringLayout.WEST, timeLine, 0, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, timeLine, 50, SpringLayout.SOUTH, contentPane);





        repaint();
        revalidate();


    }


    public static ArrayList<AnimationManager> loadAll() {
        ArrayList<AnimationManager> animationManagers = new ArrayList<>();
        File directory = new File("AnimationManagers");
        for (File file : directory.listFiles()) {
            if (file.getName().contains(".json")) {
                if (!file.getName().contains("PixelLayout")) {
                    animationManagers.add(AnimationManager.load(file));
                }
            }
        }


        return animationManagers;
    }




    public static void main(String[] args) {
        new AnimationCreator(AnimationManager.load(new File("AnimationManager.json")), false);
    }






}

class TimeLinePanel extends JPanel implements ActionListener, MouseMotionListener{
    ArrayList<Pixel> pixelArrayList = new ArrayList<>();
    private AnimationManager animationManager;
    private BufferedImage backgroundImage;
    private Timer timer = new Timer(1,this);
    private Animation selected = null;

    public TimeLinePanel(AnimationManager animationManager, ArrayList<ScreenSide> screenSides) {
        this.animationManager = animationManager;

        for (ScreenSide screenSide : screenSides) {
            pixelArrayList.addAll(screenSide.getPixels());
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
                    int stapgrootte = getHeight() / pixelArrayList.size();
                double selectedTime = mouseEvent.getX() / (getWidth() / animationManager.getTotalTime());
                    selected = null;

                    int positie = (int) (Math.floor(mouseEvent.getY() / stapgrootte));
                    for (Animation animation : animationManager.getTimeLine()) {
                        if (animation.getStartTime() < selectedTime && animation.getAfterTime() > selectedTime) {
                            if (search(animation.getPixel()) == positie) {
                                selected = animation;
                                if (mouseEvent.getClickCount() == 2) {
                                    AnimationForm.editAnimation(animation);

                                }
                            }
                        }
                    }
                    if (selected == null) {
                        animationManager.setCurrentTime((int) selectedTime);
                        repaint();

                        if (mouseEvent.getClickCount() == 2) {
                            int positie2 = (int) (Math.floor(mouseEvent.getY() / (getHeight() / pixelArrayList.size())));
                            Animation animation = new Animation(pixelArrayList.get(positie2), Color.white);
                            AnimationForm.editAnimation(animation);
                            animationManager.addAnimation(animation.getStartTime(), animation.getAfterTime(), animation);
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
            double selectedTime = mouseEvent.getX() / (getWidth() / animationManager.getTotalTime());
            int time = (int) (Math.floor(selectedTime));             //      TODO SOLUTION FOR WRONG X POSITION WHEN DRAGGING
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

        //if(!animationManager.isRunning()){
        //  animationManager.setCurrentTime(animationManager.getCurrentTime() + 0.01);
        //}

        //  TODO INTERFERING

      /*  Iterator<Animation> pixelIterator = animationManager.getTimeLine().iterator();
        while(pixelIterator.hasNext()){
            if(pixelIterator.next().isRemove()){
                pixelIterator.remove();
            }
        }*/

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


        double x = 0;
        double y = 0;
        double stap = (getHeight() - 50) / (double) pixelArrayList.size();
        Color[] colors = { new Color(163,255,78), new Color(86,214,255)};

        graphics2D.setFont(new Font("Arial", Font.BOLD, (int) stap));



        for(int i = 0; i < pixelArrayList.size(); i++){
            Rectangle2D rectangle2D = new Rectangle2D.Double(x,y,getWidth(),y+stap);
            y += stap;
            graphics2D.setColor(colors[i%colors.length]);
            graphics2D.fill(rectangle2D);

            graphics2D.setColor(colors[(i + 1) % colors.length]);
            //graphics2D.setColor(Color.black);
            graphics2D.drawString("Pixel " + pixelArrayList.get(i).getId(), (int) (getWidth() / 2.0) - graphics2D.getFont().getSize(), (int) (y - (graphics2D.getFont().getSize() / 8)));
        }

        for(Animation animation : animationManager.getTimeLine()){
            if (animation.isRemove()) {
                continue;
            }
            double calculation = getWidth() / animationManager.getTotalTime();
            Rectangle2D animationRectangle = new Rectangle2D.Double(calculation*animation.getStartTime(),
                                                                    (search(animation.getPixel()))*stap,
                                                                    calculation*animation.getAfterTime()-calculation*animation.getStartTime(),
                                                                    stap);
            //Color color = colors[(search(animation.getPixel())%colors.length)];
            Color color = animation.getColor();
            graphics2D.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
            if (Math.floor(animationManager.getCurrentTime()) >= animation.getStartTime() && Math.floor(animationManager.getCurrentTime()) < animation.getAfterTime()) {
                graphics2D.setColor(animation.getColor());
            }
            graphics2D.fill(animationRectangle);

            graphics2D.setColor(Color.black);
            graphics2D.draw(animationRectangle);

        }

        Rectangle2D rectangle2D = new Rectangle2D.Double((getWidth() / animationManager.getTotalTime()) * animationManager.getCurrentTime(), 0, 10, getHeight());

        graphics2D.setColor(new Color(0,0,0,128));
        graphics2D.fill(rectangle2D);

    }


}


