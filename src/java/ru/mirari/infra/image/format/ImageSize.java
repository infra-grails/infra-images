package ru.mirari.infra.image.format;

/**
 * Wraps image size and dexterity
 *
 * @author alari
 * @since 12/21/12 9:15 PM
 */
public class ImageSize {
    final int effectiveWidth;
    final int effectiveHeight;
    final float dexterity;

    public ImageSize(int effectiveWidth, int effectiveHeight, float dexterity) {
        this.effectiveWidth = effectiveWidth;
        this.effectiveHeight = effectiveHeight;
        this.dexterity = dexterity;
    }

    public int getEffectiveWidth() {
        return effectiveWidth;
    }

    public int getEffectiveHeight() {
        return effectiveHeight;
    }

    public float getDexterity() {
        return dexterity;
    }


    public int getWidth() {
        return (int)Math.floor(effectiveWidth/dexterity);
    }

    public int getHeight() {
        return (int)Math.floor(effectiveHeight/dexterity);
    }
}
