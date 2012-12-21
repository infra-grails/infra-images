package ru.mirari.infra.image.format;

import java.util.Map;

/**
 * @author alari
 * @since 12/21/12 9:37 PM
 */
public class ImageBundle {
    private final Map<String,ImageFormat> formats;
    private final ImageFormat basesFormat;
    private final String name;

    public ImageBundle(String name, Map<String, ImageFormat> formats, ImageFormat basesFormat){
        this.name = name;
        this.formats = formats;
        this.basesFormat = basesFormat;
    }
}
