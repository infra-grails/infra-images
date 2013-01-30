package infra.images.format;

import infra.images.util.ImageCropPolicy;
import infra.images.util.ImageSize;
import infra.images.util.ImageType;

/**
 * @author alari
 * @since 12/21/12 4:42 PM
 */
abstract public class ImageFormat {
    abstract public String getName();

    abstract public ImageCropPolicy getCrop();

    abstract public ImageType getType();

    abstract public float getQuality();

    abstract public ImageSize getSize();

    public final float getDensity() {
        return getSize().getDensity();
    }

    /**
     * Finds an actual crop policy among provided value, base format, and default fallback
     *
     * @param cropPolicy
     * @param baseFormat
     * @return
     */
    protected ImageCropPolicy findActualCrop(final ImageCropPolicy cropPolicy, final ImageFormat baseFormat) {
        if (cropPolicy != ImageCropPolicy.DEFAULT) {
            return cropPolicy;
        }
        if (baseFormat != null && baseFormat.getCrop() != ImageCropPolicy.DEFAULT) {
            return baseFormat.getCrop();
        }
        return ImageCropPolicy.NONE;
    }

    /**
     * Finds an actual image type among provided value, base format, and default fallback
     *
     * @param imageType
     * @param baseFormat
     * @return
     */
    protected ImageType findActualType(final ImageType imageType, final ImageFormat baseFormat) {
        if (imageType != ImageType.DEFAULT) {
            return imageType;
        }
        if (baseFormat != null && baseFormat.getType() != ImageType.DEFAULT) {
            return baseFormat.getType();
        }
        return ImageType.JPG;
    }

    /**
     * Finds an actual pixel density among provided value, base format, and default fallback
     *
     * @param density
     * @param baseFormat
     * @return
     */
    protected float findActualDensity(float density, final ImageFormat baseFormat) {
        if (density > 0) {
            return density;
        }
        if (baseFormat != null && baseFormat.getSize().getDensity() > 0) {
            return baseFormat.getSize().getDensity();
        }
        return 1;
    }

    /**
     * Finds an actual image quality among provided value, base format, and default fallback
     *
     * @param quality
     * @param baseFormat
     * @return
     */
    protected float findActualQuality(float quality, final ImageFormat baseFormat) {
        if (quality > 0) {
            return quality;
        }
        if (baseFormat != null && baseFormat.getQuality() > 0) {
            return baseFormat.getQuality();
        }
        return 1;
    }

    /**
     * Returns filename to store image of this format in
     *
     * @return
     */
    public String getFilename() {
        return getName() + "." + getType().toString();
    }
}
