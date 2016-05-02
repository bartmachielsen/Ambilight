import ArduinoConnector.ArduinoConnector;
import NeopixelLayout.Configuration;
import NeopixelLayout.LayoutGUI;

/**
 * Created by Bart on 28-4-2016.
 */
public class Main {
    public Main(){
        Configuration configuration;
        if ((configuration = Configuration.load()) == null) {
            configuration = new Configuration();
            configuration.updateAmbilight();
            new LayoutGUI(configuration);
        }else{
            AmbiLoader ambiLoader = new AmbiLoader(configuration, new ArduinoConnector());

            try {
                Thread.sleep(6000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ambiLoader.start();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}

