package infra.images.format;

import infra.images.annotations.Format;
import infra.images.util.ImageCropPolicy;
import infra.images.util.ImageSize;
import infra.images.util.ImageType;

/**
 * Wraps @Format annotation with fallback on bases
 *
 * @author alari
 * @since 12/21/12 5:17 PM
 */
public class AnnotationFormat extends ImageFormat {
    private final String name;
    private final ImageCropPolicy crop;
    private final ImageType type;
    private final float quality;
    private final ImageSize size;

    public AnnotationFormat(Format format, final ImageFormat baseFormat) {
        name = format.name();
        size = ImageSize.buildFormat(format.width(), format.height(), findActualDensity(format.density(), baseFormat));

        crop = findActualCrop(format.crop(), baseFormat);
        type = findActualType(format.type(), baseFormat);
        quality = findActualQuality(format.quality(), baseFormat);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageCropPolicy getCrop() {
        return crop;
    }

    @Override
    public ImageType getType() {
        return type;
    }

    @Override
    public float getQuality() {
        return quality;
    }

    @Override
    public ImageSize getSize() {
        return size;
    }
}
