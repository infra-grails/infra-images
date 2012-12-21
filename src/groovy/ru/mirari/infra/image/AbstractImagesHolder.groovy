package ru.mirari.infra.image

import ru.mirari.infra.file.FileStorage
import ru.mirari.infra.image.format.ImageFormat
import ru.mirari.infra.image.formatter.ImageFormatter

/**
 * @author alari
 * @since 12/21/12 4:28 PM
 */
abstract class AbstractImagesHolder {
    abstract FileStorage getFileStorage()
    abstract ImageFormatter getImageFormatter()

    def store(File imageFile, String imageName, String formatName = null) {

    }

    def store(File imageFile, ImageFormat format) {
      fileStorage.store(imageFormatter.format(imageFile, format), )
    }

    String getFileName(ImageFormat format) {

    }
}
