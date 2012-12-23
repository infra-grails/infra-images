package ru.mirari.infra.image.formatter;

import ru.mirari.infra.image.format.ImageFormat;
import ru.mirari.infra.image.util.ImageBox;

import java.io.IOException;

/**
 * @author alari
 * @since 12/21/12 4:43 PM
 */
abstract public class ImageFormatter {
    final static public int MODE_BUFFERED = 1;
    final static public int MODE_FILE = 2;

    abstract public ImageBox format(ImageFormat format, ImageBox original, int mode) throws IOException;
}
