package infra.images.util;

/**
 * @author Dmitry Kurinskiy
 * @since 21.10.11 14:53
 */
public enum ImageType {
    PNG("png"),
    JPG("jpg"),
    GIF("gif"),
    DEFAULT("");

    private final String name;

    static private final boolean isOpenJDK;

    static {
        isOpenJDK = System.getProperty("java.vm.name").contains("OpenJDK");
    }

    ImageType(String name) {
        this.name = name;
    }

    public String toString() {
        if (this == DEFAULT) {
            throw new IllegalStateException("You cannot use ImageType.DEFAULT as a real image type; it's just a placeholder for annotations!");
        }
        return isOpenJDK ? "png" : name;
    }
}