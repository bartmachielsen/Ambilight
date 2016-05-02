package DataStructure;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by Bart on 30-4-2016.
 */
public class Configuration {
    private File file = new File("PixelLayout.json");
    private ArrayList<ScreenConfiguration> layoutGraphicses = new ArrayList<>();

    public static Configuration load(File file) {
        try {
            Gson gson = new Gson();
            JsonReader jsonReader = gson.newJsonReader(new FileReader(file));
            Configuration configuration = gson.fromJson(jsonReader, Configuration.class);

            for (ScreenConfiguration screenConfiguration : configuration.getLayoutGraphicses()) {
                screenConfiguration.reload();
            }
            return configuration;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Configuration load() {
        return load(new File("PixelLayout.json"));
    }

    public ArrayList<ScreenConfiguration> getLayoutGraphicses() {
        return layoutGraphicses;
    }

    public void addLayoutGraphics(ScreenConfiguration screenConfiguration) {
        layoutGraphicses.add(screenConfiguration);
    }

    public void updateAmbilight() {
        for (ScreenConfiguration screenConfiguration : layoutGraphicses) {
            screenConfiguration.generateLight();
        }

    }

    public void save(){
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new Gson().toJson(this));
            fileWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

