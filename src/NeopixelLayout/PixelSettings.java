package NeopixelLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Bart Machielsen on 30-4-2016.
 */
public class PixelSettings extends JPanel {
    public PixelSettings(ScreenPanel[] screenPanels, LayoutGUI layoutGUI, Configuration configuration) {
        super();
        setLayout(new GridLayout(1, 3));
        setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        JPanel savePanel = new JPanel();
        add(savePanel);
        savePanel.setLayout(new FlowLayout());
        JButton saveButton = new JButton("SAVE");
        savePanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                configuration.save();
            }
        });

        String[] spinners = {"Bovenkant:   ", "Rechterkant: ", "Onderkant:   ", "Linkerkant:  "};
        JTabbedPane tabbedPane = new JTabbedPane();
        for (ScreenPanel screenPanel : screenPanels) {
            JPanel tab = new JPanel();
            tabbedPane.addTab("D" + screenPanel.getGraphicsDevice().getIDstring().split("D")[1], tab);

            tab.setLayout(new GridLayout(4, 1));
            int i = 0;
            for (ScreenSide screenSide : screenPanel.getSides()) {
                JPanel sideTab = new JPanel();
                sideTab.setLayout(new FlowLayout());

                sideTab.add(new JLabel(spinners[i] + "      "));

                JSpinner spinner = new JSpinner(new SpinnerNumberModel(screenSide.getPixels().size(), 0, 20, 1));
                sideTab.add(spinner);
                spinner.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (screenSide.getPixels().size() != (int) spinner.getValue()) {
                            screenSide.setPixelSize((int) spinner.getValue());
                            layoutGUI.generateNumbers();
                            screenPanel.generate();
                            layoutGUI.repaint();
                        }

                    }
                });

                JCheckBox checkBox = new JCheckBox("Actief");
                checkBox.setSelected(screenSide.isVisible());
                sideTab.add(checkBox);
                checkBox.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (!(checkBox.isSelected() == screenSide.isVisible())) {
                            screenSide.setVisible(checkBox.isSelected());
                            layoutGUI.generateNumbers();
                            layoutGUI.repaint();
                        }
                    }
                });


                tab.add(sideTab);
                i++;
            }

        }
        add(tabbedPane);


        add(new JPanel());
        add(new JPanel());

    }
}
