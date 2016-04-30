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
    public PixelSettings(LayoutPanel[] layoutPanels, LayoutGUI layoutGUI, LayoutSaver layoutSaver) {
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
                layoutSaver.save();
            }
        });

        String[] spinners = {"Bovenkant:   ", "Rechterkant: ", "Onderkant:   ", "Linkerkant:  "};
        JTabbedPane tabbedPane = new JTabbedPane();
        for (LayoutPanel layoutPanel : layoutPanels) {
            JPanel tab = new JPanel();
            tabbedPane.addTab("D" + layoutPanel.getGraphicsDevice().getIDstring().split("D")[1], tab);

            tab.setLayout(new GridLayout(4, 1));
            int i = 0;
            for (Side side : layoutPanel.getSides()) {
                JPanel sideTab = new JPanel();
                sideTab.setLayout(new FlowLayout());

                sideTab.add(new JLabel(spinners[i] + "      "));

                JSpinner spinner = new JSpinner(new SpinnerNumberModel(side.getPixels().size(), 0, 20, 1));
                sideTab.add(spinner);
                spinner.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (side.getPixels().size() != (int) spinner.getValue()) {
                            side.setPixelSize((int) spinner.getValue());
                            layoutGUI.generateNumbers();
                            layoutPanel.generate();
                            layoutGUI.repaint();
                        }

                    }
                });

                JCheckBox checkBox = new JCheckBox("Actief");
                checkBox.setSelected(side.isVisible());
                sideTab.add(checkBox);
                checkBox.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if(!(checkBox.isSelected() == side.isVisible())) {
                            side.setVisible(checkBox.isSelected());
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
