package ru.mirari.infra.image

import ru.mirari.infra.image.format.ImageFormat

/**
 * @author alari
 * @since 12/23/12 4:00 PM
 */
abstract class ImageManager {
    abstract store(File image)

    abstract getSrc()

    abstract getSrc(String formatName)

    abstract getSrc(ImageFormat format)

    abstract void delete()

    abstract getSize()

    abstract getSize(String formatName)

    abstract getSize(ImageFormat format)
}
