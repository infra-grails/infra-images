package infra.images.util;

import infra.images.format.ImageFormat;
import infra.images.format.OriginalFormat;

import java.util.Map;

/**
 * @author alari
 * @since 12/21/12 9:37 PM
 */
public class ImageFormatsBundle {
    private final Map<String, ImageFormat> formats;
    private final ImageFormat basesFormat;
    private final String name;
    private final ImageFormat original;

    public ImageFormatsBundle(String name, Map<String, ImageFormat> formats, ImageFormat basesFormat) {
        this.name = name;
        this.basesFormat = basesFormat;
        this.original = new OriginalFormat(this.name, this.basesFormat);
        formats.put(name, this.original);
        this.formats = formats;
    }

    public Map<String, ? extends ImageFormat> getFormats() {
        return formats;
    }

    public ImageFormat getBasesFormat() {
        return basesFormat;
    }

    public String getName() {
        return name;
    }

    public ImageFormat getOriginal() {
        return original;
    }
}
