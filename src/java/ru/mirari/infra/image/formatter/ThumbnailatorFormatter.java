package ru.mirari.infra.image.formatter;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Component;
import ru.mirari.infra.image.format.ImageFormat;
import ru.mirari.infra.image.util.ImageBox;
import ru.mirari.infra.image.util.ImageCropPolicy;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author alari
 * @since 12/21/12 4:54 PM
 */
@Component
public class ThumbnailatorFormatter extends ImageFormatter {
    @Override
    public ImageBox format(ImageFormat format, final ImageBox original, int mode) throws IOException {
        Thumbnails.Builder<BufferedImage> builder = Thumbnails.of(original.getBuffered())
                .size(format.getSize().getRealWidth(), format.getSize().getRealHeight())
                .outputQuality(format.getQuality())
                .outputFormat(format.getType().toString());
        if (!format.getCrop().isNoCrop()) {
            return new ImageBox(builder.crop(getPosition(format.getCrop())).asBufferedImage(), null);
        }
        return new ImageBox(builder.crop(getPosition(format.getCrop())).asBufferedImage(), format.getSize());
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
