package infra.images.formatter;

import infra.images.format.ImageFormat;
import infra.images.util.ImageBox;
import infra.images.util.ImageCropPolicy;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Component;

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
    public ImageBox format(ImageFormat format, final ImageBox original) throws IOException {
        Thumbnails.Builder<BufferedImage> builder = Thumbnails.of(original.getBuffered())
                .outputQuality(format.getQuality())
                .outputFormat(format.getType().toString());

        int h = format.getSize().getRealHeight();
        if (h <= 0) h = original.getSize().getRealHeight();

        int w = format.getSize().getRealWidth();
        if (w <= 0) w = original.getSize().getRealWidth();

        builder.size(w, h);

        File imageFile = File.createTempFile("thmbl", format.getFilename());

        if (!format.getCrop().isNoCrop()) {
            builder.crop(getPosition(format.getCrop())).toFile(imageFile);
        } else {
            builder.toFile(imageFile);
        }

        return new ImageBox(imageFile);
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
