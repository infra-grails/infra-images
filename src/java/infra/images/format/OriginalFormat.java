package infra.images.format;

import infra.images.util.ImageCropPolicy;
import infra.images.util.ImageSize;
import infra.images.util.ImageType;

/**
 * @author alari
 * @since 1/16/13 1:43 PM
 */
public class OriginalFormat extends ImageFormat {
    private final String name;
    private final ImageType type;
    private final float quality;
    static private final ImageSize SIZE = ImageSize.buildFormat(0, 0, 1);

    public OriginalFormat(String name, final ImageFormat baseFormat) {
        this.name = name;
        type = findActualType(ImageType.DEFAULT, baseFormat);
        quality = findActualQuality(0f, baseFormat);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageCropPolicy getCrop() {
        return ImageCropPolicy.NONE;
    }

    @Override
    public ImageType getType() {
        return this.type;
    }

    @Override
    public float getQuality() {
        return this.quality;
    }

    @Override
    public ImageSize getSize() {
        return SIZE;
    }
}
