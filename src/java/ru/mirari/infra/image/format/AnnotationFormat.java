package ru.mirari.infra.image.format;

import ru.mirari.infra.image.annotations.Format;

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
    private final float dexterity;
    private final int width;
    private final int height;

    public AnnotationFormat(Format format, final ImageFormat baseFormat) {
        name = format.name();
        width = format.width();
        height = format.height();

        crop = findActualCrop(format.crop(), baseFormat);
        type = findActualType(format.type(), baseFormat);
        quality = findActualQuality(format.quality(), baseFormat);
        dexterity = findActualDexterity(format.dexterity(), baseFormat);
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
    public float getDexterity() {
        return dexterity;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
