package infra.images.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author alari
 * @since 12/23/12 3:21 PM
 */
public class ImageBox {
    private volatile BufferedImage buffered = null;
    private File file;
    private ImageSize size = null;

    public ImageBox(File image) {
        this(image, null);
    }

    public ImageBox(File image, ImageSize size) {
        this.size = size;
        this.file = image;
    }

    public BufferedImage getBuffered() throws IOException {
        if (buffered == null) {
            synchronized (this) {
                if (buffered == null) {
                    buffered = ImageIO.read(file);
                }
            }
        }
        return buffered;
    }

    public File getFile() {
        return file;
    }

    public ImageSize getSize() {
        if (size == null) {
            try {
                size = ImageSize.buildReal(
                        getBuffered().getWidth(),
                        getBuffered().getHeight());
            } catch (IOException e) {
                size = new ImageSize(0, 0, 0f);
            }
        }
        return size;
    }
}
