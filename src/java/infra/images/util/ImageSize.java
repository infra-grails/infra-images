package infra.images.util;

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

    static public ImageSize buildReal(int realWidth, int realHeight) {
        return new ImageSize(realWidth, realHeight, 1f);
    }

    static public ImageSize buildReal(int realWidth, int realHeight, float density) {
        return new ImageSize(realWidth, realHeight, density);
    }

    static public ImageSize buildFormat(int width, int height, float density) {
        return new ImageSize((int) Math.ceil(width * density), (int) Math.ceil(height * density), density);
    }

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
