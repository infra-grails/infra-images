package ru.mirari.infra.image;

import java.util.List;

/**
 * @author alari
 * @since 11/4/11 10:00 AM
 */
public interface ImageHolder {
    String getImagesPath();

    String getImagesBucket();

    List<ImageFormat> getImageFormats();

    ImageFormat getDefaultImageFormat();
}
