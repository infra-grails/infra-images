package ru.mirari.infra.image.formatter;

import ru.mirari.infra.image.format.ImageFormat;
import ru.mirari.infra.image.format.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author alari
 * @since 12/21/12 4:43 PM
 */
abstract public class ImageFormatter {
    abstract public BufferedImage format(BufferedImage image, ImageFormat format) throws IOException;

    abstract public void write(final BufferedImage image, File target, ImageType type, float quality) throws IOException;

    public File format(File image, ImageFormat format) throws IOException {
        File target = File.createTempFile("image", "sfx");
        BufferedImage bufferedImage = formatToBuffer(image, format);
        write(bufferedImage, target, format.getType(), format.getQuality());
        return target;
    }

    public BufferedImage formatToBuffer(final File image, final ImageFormat format) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(image);
        return format(bufferedImage, format);
    }
}
