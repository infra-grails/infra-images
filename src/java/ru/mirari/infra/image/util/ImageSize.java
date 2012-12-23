package ru.mirari.infra.image.util;

/**
 * Wraps image size and density
 *
 * @author alari
 * @since 12/21/12 9:15 PM
 */
public class ImageSize {
    final int realWidth;
    final int realHeight;
    final float density;
    final String name;

    public ImageSize(int realWidth, int realHeight, float density) {
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.density = density;
        name = Integer.toString(realWidth) + "x" + Integer.toString(realHeight) + "_" + Float.toHexString(density);
    }

    public int getRealWidth() {
        return realWidth;
    }

    public int getRealHeight() {
        return realHeight;
    }

    public float getDensity() {
        return density;
    }

    public int getWidth() {
        return (int) Math.floor(realWidth / density);
    }

    public int getHeight() {
        return (int) Math.floor(realHeight / density);
    }

    @Override
    public String toString() {
        return name;
    }
}
