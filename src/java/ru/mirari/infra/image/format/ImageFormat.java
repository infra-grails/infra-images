package ru.mirari.infra.image.format;

/**
 * @author alari
 * @since 12/21/12 4:42 PM
 */
abstract public class ImageFormat implements Comparable<ImageFormat> {
    abstract public String getName();
    abstract public ImageCropPolicy getCrop();
    abstract public ImageType getType();
    abstract public float getQuality();
    abstract public float getDexterity();
    abstract public int getWidth();
    abstract public int getHeight();

    /**
     * Returns effective image width -- taking pixel into account
     * @return
     */
    public int getEffectiveWidth() {
        return (int) Math.ceil(getWidth()*getDexterity());
    }

    /**
     * Returns effective image height -- taking dexterity into account
     * @return
     */
    public int getEffectiveHeight() {
        return (int) Math.ceil(getHeight()*getDexterity());
    }

    /**
     * Compares formats based on its sizes
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(ImageFormat o) {
        return getWidth() > o.getWidth() && getHeight() > o.getHeight() ? 1 : -1;
    }

    /**
     * Finds an actual crop policy among provided value, base format, and default fallback
     *
     * @param cropPolicy
     * @param baseFormat
     * @return
     */
    protected ImageCropPolicy findActualCrop(final ImageCropPolicy cropPolicy, final ImageFormat baseFormat) {
        if(cropPolicy != ImageCropPolicy.DEFAULT) {
            return cropPolicy;
        }
        if(baseFormat != null && baseFormat.getCrop() != ImageCropPolicy.DEFAULT) {
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
        if(imageType == ImageType.DEFAULT) {
            return imageType;
        }
        if (baseFormat != null && baseFormat.getType() != ImageType.DEFAULT) {
            return baseFormat.getType();
        }
        return ImageType.JPG;
    }

    /**
     * Finds an actual pixel dexterity among provided value, base format, and default fallback
     *
     * @param dexterity
     * @param baseFormat
     * @return
     */
    protected float findActualDexterity(float dexterity, final ImageFormat baseFormat) {
        if(dexterity > 0) {
            return dexterity;
        }
        if (baseFormat != null && baseFormat.getDexterity() > 0) {
            return baseFormat.getDexterity();
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
        if(quality > 0) {
            return quality;
        }
        if (baseFormat != null && baseFormat.getQuality() > 0) {
            return baseFormat.getQuality();
        }
        return 1;
    }
}
