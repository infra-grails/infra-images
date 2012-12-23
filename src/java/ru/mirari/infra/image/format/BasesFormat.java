package ru.mirari.infra.image.format;

import ru.mirari.infra.image.annotations.BaseFormat;
import ru.mirari.infra.image.util.ImageCropPolicy;
import ru.mirari.infra.image.util.ImageSize;
import ru.mirari.infra.image.util.ImageType;

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
    private final ImageSize size;

    public BasesFormat(BaseFormat... baseFormats) {

        // check from bases if default
        ImageCropPolicy cropPolicy = ImageCropPolicy.DEFAULT;
        ImageType imageType = ImageType.DEFAULT;
        float imageDensity = -1;
        float imageQuality = -1;
        for (BaseFormat baseFormat : baseFormats) {
            if (cropPolicy == ImageCropPolicy.DEFAULT && baseFormat.crop() != ImageCropPolicy.DEFAULT) {
                cropPolicy = baseFormat.crop();
            }
            if (imageType == ImageType.DEFAULT && baseFormat.type() != ImageType.DEFAULT) {
                imageType = baseFormat.type();
            }
            if (imageDensity < 0 && baseFormat.density() > 0) {
                imageDensity = baseFormat.density();
            }
            if (imageQuality < 0 && baseFormat.quality() > 0) {
                imageQuality = baseFormat.quality();
            }
        }

        crop = cropPolicy;
        type = imageType;
        quality = imageQuality;
        size = new ImageSize(0, 0, imageDensity);
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


    public ImageSize getSize() {
        return size;
    }
}
