package ru.mirari.infra.image;

import net.coobird.thumbnailator.geometry.Positions;

/**
 * @author Dmitry Kurinskiy
 * @since 21.10.11 14:58
 */
public enum ImageCropPolicy {
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

    boolean isNoCrop() {
        return policy == 0;
    }

    boolean isTop() {
        return (policy & 12) == 4;
    }

    boolean isBottom() {
        return (policy & 12) == 8;
    }

    boolean isLeft() {
        return (policy & 3) == 1;
    }

    boolean isRight() {
        return (policy & 3) == 2;
    }

    Positions getPosition() {
        switch (this) {
            case TOP_LEFT:
                return Positions.TOP_LEFT;
            case TOP_CENTER:
                return Positions.TOP_CENTER;
            case TOP_RIGHT:
                return Positions.TOP_RIGHT;
            case CENTER_LEFT:
                return Positions.CENTER_LEFT;
            case CENTER_RIGHT:
                return Positions.CENTER_RIGHT;
            case CENTER:
                return Positions.CENTER;
            case BOTTOM_LEFT:
                return Positions.BOTTOM_LEFT;
            case BOTTOM_RIGHT:
                return Positions.BOTTOM_RIGHT;
            case BOTTOM_CENTER:
                return Positions.BOTTOM_CENTER;

            default:
                return null;
        }
    }
}