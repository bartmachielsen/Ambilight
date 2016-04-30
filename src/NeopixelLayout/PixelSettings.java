package NeopixelLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Bart Machielsen on 30-4-2016.
 */
public class PixelSettings extends JPanel {
    public PixelSettings(LayoutPanel[] layoutPanels, LayoutGUI layoutGUI) {
        super();
        setLayout(new GridLayout(1, 3));
        setBorder(new EtchedBorder(EtchedBorder.LOWERED));


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

                        side.setVisible(checkBox.isSelected());
                        layoutGUI.generateNumbers();
                        layoutPanel.generate();
                        layoutGUI.repaint();
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
