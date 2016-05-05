package MenuConfig;

import AmbiUpdater.AmbiLoader;
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

/**
 * Created by Bart Machielsen on 2-5-2016.
 */
public class Menu extends PopupMenu {
    private boolean started = false;
    private Configuration configuration = Configuration.load();

    public Menu() {

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
        AmbiLoader ambiLoader = new AmbiLoader(configuration, new ArduinoConnector());
        cb2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println(cb2.getState());
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
                new LayoutGUI(configuration);
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


        //Add components to pop-up menu
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
            new LayoutGUI(new Configuration());
        } else {
            new Menu();
        }

    }
}
