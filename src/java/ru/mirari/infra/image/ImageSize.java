package ru.mirari.infra.image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alari
 * @since 10/31/11 11:31 PM
 */
public class ImageSize implements Comparable<ImageSize> {
    private static Map<String, ImageSize> bySize = new HashMap<String, ImageSize>();

    public final int width;
    public final int height;

    ImageSize(Integer width, int height) {
        this.width = width;
        this.height = height;
    }

    public static ImageSize getBySize(String size) {
        if (bySize.containsKey(size)) {
            return bySize.get(size);
        }
        List<Integer> list = new ArrayList<Integer>(4);
        for (String s : size.split("[\\*x]")) {
            list.add(Integer.parseInt(s));
        }
        bySize.put(size, new ImageSize(list.get(0), list.get(1)));
        return bySize.get(size);
    }

    public String toString() {
        return String.valueOf(width).concat("*").concat(String.valueOf(height));
    }

    public int compareTo(ImageSize o) {
        return width > o.width && height > o.height ? 1 : -1;
    }
}
