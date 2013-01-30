package infra.images.deprecated

import infra.file.storage.FileStorageService
import infra.images.annotations.BaseFormat
import infra.images.annotations.Format
import infra.images.annotations.Image
import infra.images.annotations.ImagesHolder
import infra.images.format.AnnotationFormat
import infra.images.format.BasesFormat
import infra.images.util.ImageFormatsBundle

/**
 * @author alari
 * @since 12/21/12 9:36 PM
 */
@Deprecated
class AnnotatedImagesManagerProvider {
    private final BasesFormat basesFormat
    private Map<String, ImageFormatsBundle> images = [:]
    private FileStorageService fileStorageService

    AnnotatedImagesManagerProvider(Class annotatedClass, FileStorageService fileStorageService) {
        ImagesHolder holder = annotatedClass.getAnnotation(ImagesHolder)

        this.fileStorageService = fileStorageService

        BaseFormat base = holder.baseFormat()
        basesFormat = new BasesFormat(base)

        for (Image image : holder.images()) {
            Map<String, AnnotationFormat> formats = [:]
            BasesFormat imageBase = new BasesFormat(base, image.baseFormat())
            for (Format format : image.formats()) {
                formats.put(format.name(), new AnnotationFormat(format, imageBase))
            }
            images.put(image.name(), new ImageFormatsBundle(image.name(), formats, imageBase))
        }
    }

    ImagesManager getManager(def domain) {}
}
