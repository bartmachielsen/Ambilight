package NeopixelLayout;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Bart on 30-4-2016.
 */
public class LayoutSaver {
    private File file = new File("PixelLayout.json");
    private ArrayList<LayoutGraphics> layoutGraphicses = new ArrayList<>();

    public LayoutSaver(){

    }

    public ArrayList<LayoutGraphics> getLayoutGraphicses() {
        return layoutGraphicses;
    }

    public void setLayoutGraphicses(ArrayList<LayoutGraphics> layoutGraphicses) {
        this.layoutGraphicses = layoutGraphicses;
    }
    public void addLayoutGraphics(LayoutGraphics layoutGraphics){
        layoutGraphicses.add(layoutGraphics);
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
    public static LayoutSaver load(File file){
        try{
            Gson gson = new Gson();
            JsonReader jsonReader = gson.newJsonReader(new FileReader(file));
            return gson.fromJson(jsonReader,LayoutSaver.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static LayoutSaver load(){
        return load(new File("PixelLayout.json"));
    }
}

