package ru.mirari.infra.image.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author alari
 * @since 12/23/12 3:21 PM
 */
public class ImageBox {
    private BufferedImage buffered;
    private volatile ImageSize size;

    public ImageBox(BufferedImage image, ImageSize size) {
        this.size = size;
        this.buffered = image;
    }

    public ImageBox(File image, ImageSize size) throws IOException {
        this.size = size;
        this.buffered = ImageIO.read(image);
    }

    public BufferedImage getBuffered() {
        return buffered;
    }

    public ImageSize getSize() {
        if (size == null) {
            synchronized (this) {
                if (size == null) {
                    size = new ImageSize(buffered.getWidth(), buffered.getHeight(), 1f);
                }
            }
        }
        return size;
    }
}
