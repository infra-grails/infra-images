package ru.mirari.infra.image.format;

/**
 * Wraps image size and density
 *
 * @author alari
 * @since 12/21/12 9:15 PM
 */
public class ImageSize {
    final int effectiveWidth;
    final int effectiveHeight;
    final float density;

    public ImageSize(int effectiveWidth, int effectiveHeight, float density) {
        this.effectiveWidth = effectiveWidth;
        this.effectiveHeight = effectiveHeight;
        this.density = density;
    }

    public int getEffectiveWidth() {
        return effectiveWidth;
    }

    public int getEffectiveHeight() {
        return effectiveHeight;
    }

    public float getDensity() {
        return density;
    }


    public int getWidth() {
        return (int)Math.floor(effectiveWidth/ density);
    }

    public int getHeight() {
        return (int)Math.floor(effectiveHeight/ density);
    }
}
