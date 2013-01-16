package infra.images.util;

/**
 * @author Dmitry Kurinskiy
 * @since 21.10.11 14:58
 */
public enum ImageCropPolicy {
    DEFAULT(15),
    NONE(0),
    TOP_LEFT(5),
    TOP_CENTER(7),
    TOP_RIGHT(6),
    CENTER_LEFT(13),
    CENTER_RIGHT(14),
    CENTER(15),
    BOTTOM_LEFT(9),
    BOTTOM_RIGHT(10),
    BOTTOM_CENTER(11);

    private byte policy;

    ImageCropPolicy(int policy) {
        this.policy = (byte) policy;
    }

    public boolean isDefault() {
        return policy == 15;
    }

    public boolean isNoCrop() {
        return policy == 0;
    }

    public boolean isTop() {
        return (policy & 12) == 4;
    }

    public boolean isBottom() {
        return (policy & 12) == 8;
    }

    public boolean isLeft() {
        return (policy & 3) == 1;
    }

    public boolean isRight() {
        return (policy & 3) == 2;
    }
}