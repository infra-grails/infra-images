package ru.mirari.infra.image.format;

import ru.mirari.infra.image.util.ImageCropPolicy;
import ru.mirari.infra.image.util.ImageSize;
import ru.mirari.infra.image.util.ImageType;

/**
 * @author alari
 * @since 12/23/12 2:24 AM
 */
public class CustomImageFormat extends ImageFormat {
    private volatile String name;
    private ImageCropPolicy crop = ImageCropPolicy.DEFAULT;
    private ImageType type = ImageType.DEFAULT;
    private float quality = -1;
    private ImageSize size;

    public CustomImageFormat(int width, int height, float density) {
        size = new ImageSize(width, height, density);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCrop(ImageCropPolicy crop) {
        this.crop = crop;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }

    @Override
    public String getName() {
        if (name == null || name.isEmpty()) {
            synchronized (this) {
                if (name == null || name.isEmpty()) {
                    name = crop.name() + "-";
                    name += size.toString();
                    name += "_" + Float.toHexString(quality);
                }
            }
        }
        return name;
    }

    @Override
    public String toString() {
        return getName();
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

    public void setSize(ImageSize size) {
        this.size = size;
    }
}
