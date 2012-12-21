package ru.mirari.infra.image.format;

import ru.mirari.infra.image.annotations.BaseFormat;

/**
 * Wraps @BaseFormat annotations to behave effectively as an image format
 *
 * @author alari
 * @since 12/21/12 7:27 PM
 */
public class BasesFormat extends ImageFormat {
    private final ImageCropPolicy crop;
    private final ImageType type;
    private final float quality;
    private final float dexterity;

    public BasesFormat(BaseFormat[] baseFormats) {

        // check from bases if default
        ImageCropPolicy cropPolicy = ImageCropPolicy.DEFAULT;
        ImageType imageType = ImageType.DEFAULT;
        float imageDexterity = -1;
        float imageQuality = -1;
        for (BaseFormat baseFormat : baseFormats) {
            if (cropPolicy == ImageCropPolicy.DEFAULT && baseFormat.crop() != ImageCropPolicy.DEFAULT) {
                cropPolicy = baseFormat.crop();
            }
            if (imageType == ImageType.DEFAULT && baseFormat.type() != ImageType.DEFAULT) {
                imageType = baseFormat.type();
            }
            if (imageDexterity < 0 && baseFormat.dexterity() > 0) {
                imageDexterity = baseFormat.dexterity();
            }
            if (imageQuality < 0 && baseFormat.quality() > 0) {
                imageQuality = baseFormat.quality();
            }
        }

        crop = cropPolicy;
        type = imageType;
        quality = imageQuality;
        dexterity = imageDexterity;
    }

    @Override
    public String getName() {
        return null;
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
    public float getDexterity() {
        return dexterity;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
