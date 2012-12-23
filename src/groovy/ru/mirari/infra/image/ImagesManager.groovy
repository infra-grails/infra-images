package ru.mirari.infra.image

import ru.mirari.infra.image.format.ImageFormat

/**
 * @author alari
 * @since 12/23/12 4:01 PM
 */
abstract class ImagesManager {
    abstract store(File image, String imageName)

    abstract getSrc(String imageName)

    abstract getSrc(String imageName, String formatName)

    abstract getSrc(String imageName, ImageFormat format)

    abstract void delete(String imageName)

    abstract void delete()
}
