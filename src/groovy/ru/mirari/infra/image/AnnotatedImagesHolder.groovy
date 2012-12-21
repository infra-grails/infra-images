package ru.mirari.infra.image

import ru.mirari.infra.FileStorageService
import ru.mirari.infra.file.AbstractFilesHolder
import ru.mirari.infra.image.annotations.BaseFormat
import ru.mirari.infra.image.annotations.Format
import ru.mirari.infra.image.annotations.Image
import ru.mirari.infra.image.annotations.ImagesHolder
import ru.mirari.infra.image.format.AnnotationFormat
import ru.mirari.infra.image.format.BasesFormat
import ru.mirari.infra.image.format.ImageBundle

/**
 * @author alari
 * @since 12/21/12 9:36 PM
 */
class AnnotatedImagesHolder {
    private final BasesFormat basesFormat
    private Map<String,ImageBundle> images = [:]

    final domain
    final AbstractFilesHolder filesHolder
    private FileStorageService fileStorageService

    AnnotatedImagesHolder(final def domain, FileStorageService fileStorageService) {
        this.domain = domain
        ImagesHolder holder = domain.class.getAnnotation(ImagesHolder)

        this.fileStorageService = fileStorageService
        filesHolder = fileStorageService.getHolder(domain, holder.filesHolder())

        BaseFormat base = holder.baseFormat()
        basesFormat = new BasesFormat([base])

        for(Image image : holder.images()) {
            Map<String,AnnotationFormat> formats = [:]
            BasesFormat imageBase = new BasesFormat([base, image.baseFormat()])
            for(Format format : image.formats()) {
                formats.put(format.name(), new AnnotationFormat(format, imageBase))
            }
            images.put(image.name(), new ImageBundle(image.name(), formats, imageBase))
        }
    }

    void store(File image, String name){}
}
