package NeopixelLayout;

import DataStructure.Configuration;
import DataStructure.Pixel;
import DataStructure.ScreenConfiguration;
import DataStructure.ScreenSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Bart on 28-4-2016.
 */
public class LayoutGUI extends JFrame {
    private GraphicsDevice[] graphicsDevices = null;
    private ScreenPanel[] screenPanels;

    public LayoutGUI(Configuration configuration) {
        // INITIALIZING JFRAME, SETTING VISIBLE AND LAYOUT
        super("PixelLayout Manager");
        setBackground(Color.white);
        setVisible(true);
        setSize(1500, 800);
        setMinimumSize(new Dimension(1000, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // SET WINDOWS STYLED
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timer.start();



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
        springLayout.putConstraint(SpringLayout.NORTH, screenPanel, 300, SpringLayout.NORTH, contentPane);
        springLayout.putConstraint(SpringLayout.WEST, screenPanel, 0, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, screenPanel, 0, SpringLayout.SOUTH, contentPane);
        springLayout.putConstraint(SpringLayout.EAST, screenPanel, 0, SpringLayout.EAST, contentPane);

        springLayout.putConstraint(SpringLayout.NORTH, settingsPanel, 0, SpringLayout.NORTH, contentPane);
        springLayout.putConstraint(SpringLayout.WEST, settingsPanel, 0, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, settingsPanel, 0, SpringLayout.NORTH, screenPanel);
        springLayout.putConstraint(SpringLayout.EAST, settingsPanel, 0, SpringLayout.EAST, contentPane);

        // SETTINGSPANEL
        settingsPanel.setLayout(new GridLayout(1, 7));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("/t  PIXELSETTINGS");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 35));
        titlePanel.add(titleLabel);
        settingsPanel.add(titlePanel);


        //generating layout
        //                                                             TODO generate layout instead of fixed
        GraphicsDevice[] graphicsDevicesOmgedraaid = new GraphicsDevice[graphicsDevices.length];
        for (int i = 0; i < graphicsDevices.length; i++)
            graphicsDevicesOmgedraaid[i] = graphicsDevices[(graphicsDevices.length - 1) - i];

        screenPanel.setLayout(new GridLayout(1, graphicsDevices.length));
        screenPanels = new ScreenPanel[graphicsDevices.length];


        if (configuration.getLayoutGraphicses().size() > 0) {
            for (int i = 0; i < configuration.getLayoutGraphicses().size(); i++) {
                screenPanels[i] = new ScreenPanel(configuration.getLayoutGraphicses().get(i));
                screenPanel.add(screenPanels[i]);
            }
        }else {
            for (int i = 0; i < graphicsDevices.length; i++) {

                ArrayList<ScreenSide> screenSides = new ArrayList<>();

                for (int ii = 0; ii < 4; ii++)
                    screenSides.add(new ScreenSide(90 * ii));

                if (i < graphicsDevices.length - 1)
                    screenSides.get(1).setVisible(false);

                if (i > 0 && graphicsDevices.length > 1)
                    screenSides.get(3).setVisible(false);


                ScreenConfiguration screenConfiguration = new ScreenConfiguration(graphicsDevicesOmgedraaid[i]);
                screenConfiguration.setScreenSides(screenSides);
                screenPanels[i] = new ScreenPanel(screenConfiguration);
                screenPanels[i].createPixels();
                screenPanels[i].generate();
                configuration.addLayoutGraphics(screenConfiguration);
                screenPanel.add(screenPanels[i]);
            }

        }
        generateNumbers();


        //GENERATE PIXELSETTINGS SCREEN
        PixelSettings optionPanel = new PixelSettings(screenPanels, this, configuration);
        settingsPanel.add(optionPanel);




        revalidate();
        configuration.save();




    }

    public void generateNumbers() {
        int index = 0;
        int[] forced = new int[20];
        int forceindex = 0;
        for (int i = 0; i < screenPanels.length; i++) {
            for (ScreenSide screenSide : screenPanels[i].getSides())
                for (Pixel pixel : screenSide.getPixels())
                    if (pixel.isIdForced()) {
                        boolean exists = false;
                        for (int forcedExisting : forced) {
                            if (forcedExisting == pixel.getId()) {
                                exists = true;
                            }
                        }
                        if (exists) {
                            pixel.setIdForced(false);
                        } else {
                            forced[forceindex] = pixel.getId();
                            forceindex++;
                        }
                    }


            index = screenPanels[i].getSides().get(0).giveNumber(index, forced);

        }

        for (int i = 0; i < screenPanels.length; i++)
            for (int ii = 1; ii < screenPanels[i].getSides().size(); ii++)
                index = screenPanels[(screenPanels.length - 1) - i].getSides().get(ii).giveNumber(index, forced);


    }


}

