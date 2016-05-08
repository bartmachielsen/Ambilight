package Animations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Bart Machielsen on 8-5-2016.
 */
public class AnimationForm {
    public static void editAnimation(Animation animation) {
        JPanel jPanel = new JPanel(new GridLayout(6, 1));

        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(10, 0, 100000, 1);
        SpinnerNumberModel spinnerNumberModel1 = new SpinnerNumberModel(10, 0, 100000, 1);
        JSpinner startSpinner = new JSpinner(spinnerNumberModel);
        JSpinner endSpinner = new JSpinner(spinnerNumberModel1);


        jPanel.add(new JLabel("Begintijd (s)"));
        startSpinner.setValue(animation.getStartTime());
        jPanel.add(startSpinner);

        jPanel.add(new JLabel("Eindtijd (s)"));
        endSpinner.setValue(animation.getAfterTime());
        jPanel.add(endSpinner);

        jPanel.add(new JLabel("StartEffect"));
        JComboBox comboBox = new JComboBox(Effect.values());
        comboBox.setSelectedItem(animation.getStartEffect());
        jPanel.add(comboBox);

        jPanel.add(new JLabel("EindEffect"));
        JComboBox comboBox1 = new JComboBox(Effect.values());
        comboBox1.setSelectedItem(animation.getAfterEffect());
        jPanel.add(comboBox1);

        jPanel.add(new JLabel("Pixel"));
        jPanel.add(new JLabel("Pixel " + animation.getPixel().getId()));

        jPanel.add(new JLabel("Color"));
        JButton colorButton = new JButton("Pick color");
        colorButton.setBackground(animation.getColor());
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animation.setColor(JColorChooser.showDialog(null, "Pick color", animation.getColor()));
                colorButton.setBackground(animation.getColor());
            }
        });

        jPanel.add(colorButton);


        Object[] keuzes = {"Opslaan", "Verwijderen"};
        int keuze = JOptionPane.showOptionDialog(null, jPanel, "Animation editer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, keuzes, keuzes[0]);
        animation.setEffects((Effect) comboBox.getSelectedItem(), (Effect) comboBox1.getSelectedItem());
        animation.setEffectTime((int) startSpinner.getValue(), (int) endSpinner.getValue());


        if (keuze == 1) {
            animation.setRemove(true);
        }

    }

}
