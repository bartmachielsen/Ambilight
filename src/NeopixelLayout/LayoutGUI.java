package NeopixelLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Bart on 28-4-2016.
 */
public class LayoutGUI extends JFrame {
    private GraphicsDevice[] graphicsDevices = null;
    private LayoutPanel[] layoutPanels;
    public LayoutGUI(){
        // INITIALIZING JFRAME, SETTING VISIBLE AND LAYOUT
        super("PixelLayout Manager");
        setVisible(true);
        setSize(1500,600);
        setMinimumSize(new Dimension(1000,600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
        settingsPanel.setLayout(new GridLayout(1, 7));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("  PIXELSETTINGS");
        titleLabel.setFont(new Font("Helvetica" ,Font.BOLD,35));
        titlePanel.add(titleLabel);
        settingsPanel.add(titlePanel);


        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(null);
        //settingsPanel.add(optionPanel);                                                       TODO NICER UPPERBANNER


        //generating layout                                                                     TODO generate layout instead of fixed
        screenPanel.setLayout(new GridLayout(1,graphicsDevices.length));
        layoutPanels = new LayoutPanel[graphicsDevices.length];
        for(int i = 0; i < graphicsDevices.length; i++){

            if ((i + 1) >= graphicsDevices.length) {
                Side[] block = {Side.LEFT};
                layoutPanels[i] = new LayoutPanel(graphicsDevices[i], this, block);
            } else if (i == 0 && graphicsDevices.length > 1) {
                Side[] block = {Side.RIGHT};
                layoutPanels[i] = new LayoutPanel(graphicsDevices[i], this, block);
            } else {
                layoutPanels[i] = new LayoutPanel(graphicsDevices[i], this);
            }
            screenPanel.add(layoutPanels[i]);
        }


        revalidate();


    }


}

