import NeopixelLayout.LayoutGUI;
import NeopixelLayout.LayoutSaver;

/**
 * Created by Bart on 28-4-2016.
 */
public class Main {
    public static void main(String[] args) {
        new Main();
    }
    public Main(){
        LayoutSaver layoutSaver;
        if((layoutSaver = LayoutSaver.load()) == null) {
            new LayoutGUI(new LayoutSaver());
        }else{
            new LayoutGUI(layoutSaver);
        }
    }
}

