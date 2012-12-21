package ru.mirari.infra.image.deprecated;

import java.util.List;

/**
 * @author alari
 * @since 11/4/11 10:00 AM
 */
@Deprecated
public interface ImageHolderInterface {
    String getImagesPath();

    String getImagesBucket();

    List<ImageFormat> getImageFormats();

    ImageFormat getDefaultImageFormat();
}
