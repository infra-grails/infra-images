package infra.images

import infra.file.storage.FilesHolder
import infra.file.storage.FilesManager
import infra.images.annotations.Format
import infra.images.annotations.Image
import infra.images.annotations.ImageHolder
import infra.images.format.AnnotationFormat
import infra.images.format.BasesFormat
import infra.images.format.ImageFormat
import infra.images.formatter.ImageFormatter
import infra.images.util.ImageFormatsBundle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import infra.file.storage.FileStorageService

/**
 * @author alari
 * @since 12/23/12 4:06 PM
 */
@Component
class AnnotatedImageManagerProvider {
    private volatile Map<Class,Provider> providers = [:]

    @Autowired
    FileStorageService fileStorageService
    @Autowired
    ImageFormatter imageFormatter

    Provider getProvider(Class aClass, boolean withImageDomains=false) {
        if (!providers.containsKey(aClass)) {
            providers.put(aClass, new Provider(aClass, withImageDomains))
        }
        providers.get(aClass)
    }

    ImageManager getManager(def domain, boolean withImageDomains=false) {
        getProvider(domain.class, withImageDomains).getManager(domain)
    }

    void clear() {
        providers = [:]
    }

    private class Provider {
        private final ImageFormatsBundle imageBundle
        private final Class domainClass
        private final FilesHolder filesHolder

        private boolean storeDomains

        Provider(Class aClass, boolean withImageDomains) {
            storeDomains = withImageDomains

            domainClass = aClass
            ImageHolder holder = domainClass.getAnnotation(ImageHolder)

            filesHolder = holder.filesHolder()

            Image imageAnnotation = holder.image()

            BasesFormat basesFormat = new BasesFormat(imageAnnotation.baseFormat())
            Map<String, ImageFormat> formats = [:]
            for (Format format : imageAnnotation.formats()) {
                formats.put(format.name(), new AnnotationFormat(format, basesFormat))
            }

            imageBundle = new ImageFormatsBundle(imageAnnotation.name(), formats, basesFormat)
        }

        ImageManager getManager(def domain) {
            ImageManager m = new BasicImageManager(getFilesManager(domain), imageBundle, imageFormatter)
            (storeDomains ?  new DomainImageManager(m) : m)
        }

        private FilesManager getFilesManager(def domain) {
            fileStorageService.getManager(domain, storeDomains, filesHolder as FilesHolder)
        }
    }
}
