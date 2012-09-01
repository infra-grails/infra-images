package ru.mirari.infra.image;

/**
 * @author Dmitry Kurinskiy
 * @since 21.10.11 14:53
 */
public enum ImageType {
    PNG("png"),
    JPG("jpg"),
    GIF("gif");

    private final String name;

    static private final boolean isOpenJDK;

    static {
        isOpenJDK = System.getProperty("java.vm.name").contains("OpenJDK");
    }

    ImageType(String name) {
        this.name = name;
    }

    public String toString() {
        return isOpenJDK ? "png" : name;
    }
}