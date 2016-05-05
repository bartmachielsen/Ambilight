package ArduinoConnector;

import DataStructure.Pixel;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;


/**
 * Created by Bart Machielsen on 1-5-2016.
 */
public class ArduinoConnector implements SerialPortEventListener {
    private ArrayList<CommPortIdentifier> commPortIdentifiers = new ArrayList<>();
    private OutputStream output;
    private BufferedReader input;
    private CommPortIdentifier bestPort;
    private int send = 0;
    private SerialPort serialPort;

    public ArduinoConnector() {
        getAvailablePorts();
        tryPorts();
        try {
            serialPort = (SerialPort) bestPort.open(this.getClass().getName(), 2);
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    READY TO DELETE ??
    public void sendPixel(Pixel pixel) {
        try {
            byte[] array = new byte[4];
            array[0] = (byte) ((pixel.getId()));
            array[1] = (byte) (pixel.getColor().getRed() / 2.0);
            array[2] = (byte) (pixel.getColor().getGreen() / 2.0);
            array[3] = (byte) (pixel.getColor().getBlue() / 2.0);
            output.write(array);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void sendPixels(Pixel... pixels) {
        if (pixels.length > 0) {
            try {
                int i = 0;
                byte[] array = new byte[pixels.length + 3];

                for (Pixel pixel : pixels) {
                    array[i] = (byte) ((pixel.getId()) + 128);
                    i++;
                }

                array[i] = (byte) ((pixels[0].getColor().getRed() / 4.0) + 10);
                array[i + 1] = (byte) ((pixels[0].getColor().getGreen() / 4.0) + 10);
                array[i + 2] = (byte) ((pixels[0].getColor().getBlue() / 4.0) + 10);
                output.write(array, 0, array.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void getAvailablePorts() {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            commPortIdentifiers.add((CommPortIdentifier) ports.nextElement());
        }
    }

    public void tryPorts() {
        bestPort = commPortIdentifiers.get(1);      // TODO TRY METHODE!
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
