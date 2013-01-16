package infra.images.formatter;

import infra.images.format.ImageFormat;
import infra.images.util.ImageBox;

import java.io.IOException;

/**
 * @author alari
 * @since 12/21/12 4:43 PM
 */
abstract public class ImageFormatter {
    abstract public ImageBox format(ImageFormat format, ImageBox original) throws IOException;
}
