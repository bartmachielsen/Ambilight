package MenuConfig;

import AmbiUpdater.AmbiLoader;
import Animations.AnimationCreator;
import Animations.AnimationManager;
import ArduinoConnector.ArduinoConnector;
import DataStructure.Configuration;
import NeopixelLayout.LayoutGUI;
import NeopixelLayout.Virtualizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Bart Machielsen on 2-5-2016.
 */
public class Menu extends PopupMenu {
    private boolean started = false;
    private Configuration configuration = Configuration.load();
    private ArduinoConnector arduinoConnector;
    private AnimationManager selected = null;


    public Menu() {

        arduinoConnector = ArduinoConnector.selectArduinoConnector();

        /* GETTING BULBICON AND SETTING IT AS ICON*/
        TrayIcon trayIcon = null;
        try {
            Image image = ImageIO.read(new File("bulb.png"));
            trayIcon = new TrayIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CheckboxMenuItem virtualizer1 = new CheckboxMenuItem("Virtualizer");
        Virtualizer virtualizer = new Virtualizer(configuration);
        virtualizer1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (virtualizer.isVisible()) {
                    virtualizer.exit();
                } else {
                    virtualizer.show();
                }
            }
        });


        CheckboxMenuItem cb2 = new CheckboxMenuItem("Ambilight Controller");
        AmbiLoader ambiLoader = new AmbiLoader(configuration, arduinoConnector);
        cb2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (selected != null) {
                    selected.stop();
                    selected = null;
                }

                if (cb2.getState()) {
                    if (!started) {
                        started = true;
                        ambiLoader.start();
                    } else {
                        ambiLoader.restart();
                    }
                } else {
                    ambiLoader.setColor(Color.black);
                    ambiLoader.stopThread();
                }
            }
        });

        MenuItem PixelLayout = new MenuItem("Change PixelLayout");
        PixelLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LayoutGUI(configuration, false);
            }
        });


        MenuItem randomColor = new MenuItem("Random Kleur");
        randomColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ambiLoader.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
            }
        });
        MenuItem exitItem = new MenuItem("Afsluiten");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ambiLoader.setColor(Color.black);
                System.exit(0);
            }
        });


        ArrayList<AnimationManager> animationManagers = AnimationCreator.loadAll();


        //Add components to pop-up menu
        for (AnimationManager animationManager : animationManagers) {
            animationManager.reload();
            animationManager.setArduinoConnector(arduinoConnector);
            MenuItem menuItem = new MenuItem(animationManager.getName());
            add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selected != null) {
                        if (selected == animationManager) {
                            animationManager.stop();
                            selected = null;
                            ambiLoader.setColor(Color.black);
                        } else {
                            selected.stop();
                            selected = animationManager;
                            animationManager.start();
                        }
                    } else {
                        selected = animationManager;
                        animationManager.start();

                    }
                }
            });
        }
        MenuItem createSheme = new MenuItem("New Animation");
        createSheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    selected.stop();
                    selected = null;
                }
                new AnimationCreator(new AnimationManager(100, arduinoConnector), true);
            }
        });
        add(createSheme);
        addSeparator();
        add(PixelLayout);
        addSeparator();
        add(cb2);
        add(virtualizer1);
        addSeparator();
        add(randomColor);
        add(exitItem);


        trayIcon.setPopupMenu(this);

        try {

            SystemTray systemTray = SystemTray.getSystemTray();
            systemTray.add(trayIcon);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Configuration configuration;
        if ((configuration = Configuration.load()) == null) {
            new LayoutGUI(new Configuration(), true);
        } else {
            new Menu();
        }

    }
}
