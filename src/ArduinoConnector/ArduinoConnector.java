package ArduinoConnector;

import DataStructure.Pixel;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;


/**
 * Created by Bart Machielsen on 1-5-2016.
 */
public class ArduinoConnector implements SerialPortEventListener {
    private OutputStream output;
    private BufferedReader input;
    private int send = 0;
    private SerialPort serialPort;

    public ArduinoConnector() {
        connect(tryPorts(getAvailablePorts()));

    }

    public ArduinoConnector(CommPortIdentifier commPortIdentifier) {
        connect(commPortIdentifier);
    }


    public static CommPortIdentifier[] getAvailablePorts() {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        ArrayList<CommPortIdentifier> commPortIdentifiers = new ArrayList<>();
        while (ports.hasMoreElements()) {
            commPortIdentifiers.add((CommPortIdentifier) ports.nextElement());
        }

        CommPortIdentifier[] commPortIdentifiers1 = new CommPortIdentifier[commPortIdentifiers.size()];
        return commPortIdentifiers.toArray(commPortIdentifiers1);


    }

    public static ArduinoConnector selectArduinoConnector() {
        CommPortIdentifier[] commPortIdentifiers = ArduinoConnector.getAvailablePorts();
        Object[] objects = new Object[commPortIdentifiers.length];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = commPortIdentifiers[i].getName();
        }
        int choice = JOptionPane.showOptionDialog(null, "Connect to Arduino", null, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, objects, objects[0]);


        return new ArduinoConnector(commPortIdentifiers[choice]);
    }

    public static void main(String[] args) {
        ArduinoConnector.selectArduinoConnector();
    }

    public boolean connect(CommPortIdentifier commPortIdentifier) {
        try {
            serialPort = (SerialPort) commPortIdentifier.open(this.getClass().getName(), 2);
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();


            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            Thread.sleep(2000);     //      TODO BEST TIMING FOR THIS ???
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void sendPixels(Pixel... pixels) {
        if (pixels.length > 0) {
            try {
                int i = 0;
                byte[] array = new byte[pixels.length + 3];

                for (Pixel pixel : pixels) {
                    array[i] = (byte) ((pixel.getId()) + 128);
                    i++;
                }

                array[pixels.length] = (byte) (((pixels[0].getColor().getRed() / 4.0) + 10));
                array[pixels.length + 1] = ((byte) ((pixels[0].getColor().getGreen() / 4.0) + 10));
                array[pixels.length + 2] = ((byte) ((pixels[0].getColor().getBlue() / 4.0) + 10));


                output.write(array);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CommPortIdentifier tryPorts(CommPortIdentifier[] commPortIdentifiers) {
        return commPortIdentifiers[1];          //  TODO FIND WAY TO FIND OUT IF IT'S A ARDUINO OR OTHER DEVICE
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                int inputLine = input.read();
                System.out.println("RECEIVED:" + inputLine);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

}
