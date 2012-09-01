package ru.mirari.infra.image;

import net.coobird.thumbnailator.Thumbnails;

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
    private final static ImageType DEFAULT_TYPE = ImageType.JPG;
    private final static float DEFAULT_QUALITY = 0.9f;

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

    public void write(final BufferedImage image, File output) throws IOException {
        Thumbnails.of(image)
                .scale(1)
                .outputQuality(DEFAULT_QUALITY)
                .outputFormat(type.toString())
                .toFile(output);
    }

    public void reformat(File original) throws IOException {
        if (cropPolicy.isNoCrop()) {
            Thumbnails.of(original)
                    .size(size.width, size.height)
                    .outputQuality(DEFAULT_QUALITY)
                    .outputFormat(type.toString())
                    .toFile(original);
        } else {
            write(ImageIO.read(original), original);
        }
    }

    public File format(final File original) throws IOException {
        File tmp = File.createTempFile(TMP_PREFIX + name, "." + type);

        if (cropPolicy.isNoCrop()) {
            Thumbnails.of(original)
                    .size(size.width, size.height)
                    .outputQuality(DEFAULT_QUALITY)
                    .outputFormat(type.toString())
                    .toFile(tmp);
            return tmp;
        }

        write(format(ImageIO.read(original)), tmp);
        return tmp;
    }

    public void format(final BufferedImage original, File output) throws IOException {
        Thumbnails.Builder<BufferedImage> builder =
                Thumbnails.of(original)
                        .size(size.width, size.height)
                        .outputQuality(DEFAULT_QUALITY)
                        .outputFormat(type.toString());
        if (!cropPolicy.isNoCrop()) {
            builder.crop(cropPolicy.getPosition());
        }
        builder.toFile(output);
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

        try {
            if (cropPolicy.isNoCrop()) {
                return Thumbnails.of(original)
                        .size(size.width, size.height)
                        .outputQuality(DEFAULT_QUALITY)
                        .outputFormat(type.toString())
                        .asBufferedImage();
            }
            return Thumbnails.of(original)
                    .size(size.width, size.height)
                    .crop(cropPolicy.getPosition())
                    .outputQuality(DEFAULT_QUALITY)
                    .outputFormat(type.toString())
                    .asBufferedImage();
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    public int compareTo(ImageFormat o) {
        return size.compareTo(o.size);
    }
}