package ru.mirari.infra.image

import ru.mirari.infra.file.FilesHolder
import ru.mirari.infra.image.annotations.Format
import ru.mirari.infra.image.annotations.Image
import ru.mirari.infra.image.annotations.ImageHolder
import ru.mirari.infra.image.format.AnnotationFormat
import ru.mirari.infra.image.format.BasesFormat
import ru.mirari.infra.image.format.ImageFormat
import ru.mirari.infra.image.util.ImageBundle

/**
 * @author alari
 * @since 12/21/12 9:53 PM
 */
class AnnotatedImageHolder {
    ImageBundle image

    AnnotatedImageHolder(final def domain) {
        ImageHolder holder = domain.class.getAnnotation(ImageHolder)

        FilesHolder filesHolder = holder.filesHolder()
        Image imageAnnotation = holder.image()

        BasesFormat basesFormat = new BasesFormat(imageAnnotation.baseFormat())
        Map<String, ImageFormat> formats = [:]
        for (Format format : imageAnnotation.formats()) {
            formats.put(format.name(), new AnnotationFormat(format, basesFormat))
        }

        image = new ImageBundle(imageAnnotation.name(), formats, basesFormat)
    }

    void store(File image) {}

    String getSrc() {}

    String getSrc(String formatName) {}

    String getSrc(ImageFormat format) {}

    void delete() {}
}
