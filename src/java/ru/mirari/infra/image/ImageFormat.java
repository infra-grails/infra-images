package ru.mirari.infra.image;

import net.coobird.thumbnailator.Thumbnailator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Dmitry Kurinskiy
 * @since 24.10.11 13:20
 */
public class ImageFormat implements Comparable<ImageFormat> {
    private final static String TMP_PREFIX = "imageFile";
    private final static String DEFAULT_NAME = "image";
    private final static ImageCropPolicy DEFAULT_CROP = ImageCropPolicy.CENTER;
    private final static ImageType DEFAULT_TYPE = ImageType.PNG;

    public final ImageCropPolicy cropPolicy;
    public final ImageType type;
    public final String name;

    final ImageSize size;

    public ImageFormat(String maxSize) {
        this(ImageSize.getBySize(maxSize));
    }

    public ImageFormat(String maxSize, String name) {
        this(ImageSize.getBySize(maxSize), name);
    }

    public ImageFormat(ImageSize maxSize) {
        this(maxSize, DEFAULT_NAME, DEFAULT_CROP, DEFAULT_TYPE);
    }

    public ImageFormat(ImageSize maxSize, String name) {
        this(maxSize, name, DEFAULT_CROP, DEFAULT_TYPE);
    }

    public ImageFormat(String maxSize, String name, ImageCropPolicy cropPolicy,
                       ImageType type) {
        this(ImageSize.getBySize(maxSize), name, cropPolicy, type);
    }

    public ImageFormat(ImageSize maxSize, String name, ImageCropPolicy cropPolicy,
                       ImageType type) {

        this.size = maxSize;
        this.cropPolicy = cropPolicy;
        this.type = type;
        this.name = name;
    }

    public String toString() {
        return size.toString() + "-" + name + "." + type;
    }

    public void reformat(File original) throws IOException {
        if (cropPolicy.isNoCrop()) {
            Thumbnailator.createThumbnail(original, original, size.width, size.height);
        } else {
            ImageIO.write(format(ImageIO.read(original)), type.toString(), original);
        }
    }

    public File format(final File original) throws IOException {
        File tmp = File.createTempFile(TMP_PREFIX + name, "." + type);

        if (cropPolicy.isNoCrop()) {
            Thumbnailator.createThumbnail(original, tmp, size.width, size.height);
            return tmp;
        }

        ImageIO.write(format(ImageIO.read(original)), type.toString(), tmp);
        return tmp;
    }

    public BufferedImage formatToBuffer(final File original) throws IOException {
        BufferedImage image = ImageIO.read(original);
        return format(image);
    }

    public BufferedImage format(final BufferedImage original) {
        // Too small to resize
        if (original.getWidth() <= size.width && original.getHeight() <= size.height) {
            return original;
        }

        // No crop et al
        if (cropPolicy.isNoCrop()) {
            return Thumbnailator.createThumbnail(original, size.width, size.height);
        }

        // We need to deeply modify an image
        BufferedImage workingImage = original;

        // At first we resize
        ImageSize resize = calcResizeBeforeCrop(workingImage);
        // TODO: we may have no need to resize, only to crop

        workingImage = Thumbnailator.createThumbnail(workingImage, resize.width, resize.height);
        if (workingImage.getWidth() == size.width && workingImage.getHeight() == size.height) {
            return workingImage;
        }

        // Then we crop
        int x0 = 0, y0 = 0;

        int w = Math.min(size.width, workingImage.getWidth());
        int h = Math.min(size.height, workingImage.getHeight());

        if (workingImage.getWidth() > size.width) {
            // Too wide image
            if (cropPolicy.isLeft()) {
                x0 = 0;
            } else if (cropPolicy.isRight()) {
                x0 = workingImage.getWidth() - w;
            } else {
                x0 = (int) Math.ceil((workingImage.getWidth() - w) / 2);
            }
        }
        if (workingImage.getHeight() > size.height) {
            // Too tall image
            if (cropPolicy.isTop()) {
                y0 = 0;
            } else if (cropPolicy.isBottom()) {
                y0 = workingImage.getHeight() - h;
            } else {
                y0 = (int) Math.ceil((workingImage.getHeight() - h) / 2);
            }
        }

        // The job is done
        return workingImage.getSubimage(x0, y0, w, h);
    }

    private ImageSize calcResizeBeforeCrop(final BufferedImage image) {
        double yAspect = image.getHeight() / size.height;
        double xAspect = image.getWidth() / size.width;

        if (xAspect == yAspect || (xAspect < 1 && yAspect < 1)) {
            return new ImageSize(size.width, size.height);
        }

        if (xAspect < 1) {
            return new ImageSize(image.getWidth(), size.height);
        }
        if (yAspect < 1) {
            return new ImageSize(size.width, image.getHeight());
        }

        if (xAspect < yAspect) {
            return new ImageSize(size.width, (int) Math.ceil(image.getHeight() / xAspect));
        } else {
            return new ImageSize((int) Math.ceil(image.getWidth() / yAspect), size.height);
        }
    }

    public int compareTo(ImageFormat o) {
        return size.compareTo(o.size);
    }
}