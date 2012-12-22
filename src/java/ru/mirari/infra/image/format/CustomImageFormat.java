package ru.mirari.infra.image.format;

/**
 * @author alari
 * @since 12/23/12 2:24 AM
 */
public class CustomImageFormat extends ImageFormat {
    private volatile String name;
    private ImageCropPolicy crop = ImageCropPolicy.DEFAULT;
    private ImageType type = ImageType.DEFAULT;
    private float quality = -1;
    private float density = -1;
    private int width;
    private int height;

    public CustomImageFormat(int width, int height) {
        this.width = width;
        this.height = height;
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

    public void setDensity(float density) {
        this.density = density;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String getName() {
        if(name == null || name.isEmpty()) {
            synchronized (this) {
                if(name == null || name.isEmpty()) {
                    name = crop.name() + "-";
                    name += Integer.toString(width) + "x" + Integer.toString(height);
                    name += "_"+Float.toHexString(quality);
                    name += "_"+Float.toHexString(density);
                }
            }
        }
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
    public float getDensity() {
        return density;
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
