package DataStructure;

import java.util.Comparator;

/**
 * Created by Bart on 8-5-2016.
 */
public class PixelSortComparater implements Comparator<Pixel> {
    @Override
    public int compare(Pixel pixel, Pixel t1) {
        if(pixel.getId() > t1.getId()){
            return 1;
        }else{
            if(pixel.getId() < t1.getId()){
                return -1;
            }else{
                return 0;
            }
        }
    }

}

