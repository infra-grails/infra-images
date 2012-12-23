package ru.mirari.infra.image.util;

import ru.mirari.infra.image.format.ImageFormat;

import java.util.Map;

/**
 * @author alari
 * @since 12/21/12 9:37 PM
 */
public class ImageBundle {
    private final Map<String, ? extends ImageFormat> formats;
    private final ImageFormat basesFormat;
    private final String name;

    public ImageBundle(String name, Map<String, ? extends ImageFormat> formats, ImageFormat basesFormat) {
        this.name = name;
        this.formats = formats;
        this.basesFormat = basesFormat;
    }
}
