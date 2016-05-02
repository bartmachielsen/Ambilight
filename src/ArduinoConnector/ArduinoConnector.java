package ArduinoConnector;

import NeopixelLayout.Pixel;
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

    public ArduinoConnector() {
        getAvailablePorts();
        tryPorts();
        try {
            SerialPort serialPort = (SerialPort) bestPort.open(this.getClass().getName(), 200);
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPixel(Pixel pixel) {
        try {
            byte[] array = new byte[4];
            array[0] = (byte) ((pixel.getId()) - 1);
            array[1] = (byte) (pixel.getColor().getRed() / 2.0);
            array[2] = (byte) (pixel.getColor().getGreen() / 2.0);
            array[3] = (byte) (pixel.getColor().getBlue() / 2.0);
            output.write(array);
        } catch (Exception e) {
            e.printStackTrace();
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
                System.out.println(inputLine);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}
