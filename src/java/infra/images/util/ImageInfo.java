package infra.images.util;

import infra.images.format.ImageFormat;

import java.util.Map;

/**
 * @author alari
 * @since 1/19/13 11:20 AM
 */
public class ImageInfo {
    private final ImageFormat format;
    private final ImageSize size;
    private final String src;

    public ImageInfo(final ImageFormat format, final ImageSize size, final String src) {
        this.format = format;
        this.size = size;
        this.src = src;
    }

    public ImageSize getSize() {
        return size;
    }

    public ImageFormat getFormat() {
        return format;
    }

    public String getSrc() {
        return src;
    }

    public int getWidth() {
        return size.getWidth() > 0 ? size.getWidth() : format.getSize().getWidth();
    }

    public int getHeight() {
        return size.getHeight() > 0 ? size.getHeight() : format.getSize().getHeight();
    }

    public String getImg(Map<String, String> attributes) {
        if(src == null || src.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("<img src=\"").append(src).append('"');

        if (getWidth() > 0) {
            builder.append(" width=\"").append(getWidth()).append('"');
        }
        if (getHeight() > 0) {
            builder.append(" height=\"").append(getHeight()).append('"');
        }

        if (attributes != null) for (Map.Entry<String, String> attr : attributes.entrySet()) {
            builder.append(" ")
                    .append(attr.getKey())
                    .append("=\"")
                    .append(attr.getValue())
                    .append('"');
        }
        builder.append("/>");
        return builder.toString();
    }

    public String getImg() {
        return getImg(null);
    }

    public String toString() {
        return getImg();
    }
}
