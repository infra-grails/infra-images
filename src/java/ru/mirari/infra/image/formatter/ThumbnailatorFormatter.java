package ru.mirari.infra.image.formatter;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Component;
import ru.mirari.infra.image.format.ImageCropPolicy;
import ru.mirari.infra.image.format.ImageFormat;
import ru.mirari.infra.image.format.ImageType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author alari
 * @since 12/21/12 4:54 PM
 */
@Component
public class ThumbnailatorFormatter extends ImageFormatter {
    @Override
    public BufferedImage format(final BufferedImage image, ImageFormat format) throws IOException {
        // Too small to resize
        if (image.getWidth() <= format.getEffectiveWidth() && image.getHeight() <= format.getEffectiveHeight()) {
            return image;
        }

        if (format.getCrop().isNoCrop()) {
            return Thumbnails.of(image)
                    .size(format.getEffectiveWidth(), format.getEffectiveHeight())
                    .outputQuality(format.getQuality())
                    .outputFormat(format.getType().toString())
                    .asBufferedImage();
        }
        return Thumbnails.of(image)
                .size(format.getEffectiveWidth(), format.getEffectiveHeight())
                .crop(getPosition(format.getCrop()))
                .outputQuality(format.getQuality())
                .outputFormat(format.getType().toString())
                .asBufferedImage();
    }

    @Override
    public void write(final BufferedImage image, File target, ImageType type, float quality) throws IOException {
        Thumbnails.of(image)
                .scale(1)
                .outputQuality(quality)
                .outputFormat(type.toString())
                .toFile(target);
    }

    private Position getPosition(ImageCropPolicy policy) {
        switch (policy) {
            case TOP_LEFT:
                return Positions.TOP_LEFT;
            case TOP_CENTER:
                return Positions.TOP_CENTER;
            case TOP_RIGHT:
                return Positions.TOP_RIGHT;
            case CENTER_LEFT:
                return Positions.CENTER_LEFT;
            case CENTER_RIGHT:
                return Positions.CENTER_RIGHT;
            case CENTER:
                return Positions.CENTER;
            case BOTTOM_LEFT:
                return Positions.BOTTOM_LEFT;
            case BOTTOM_RIGHT:
                return Positions.BOTTOM_RIGHT;
            case BOTTOM_CENTER:
                return Positions.BOTTOM_CENTER;

            default:
                return null;
        }
    }
}
