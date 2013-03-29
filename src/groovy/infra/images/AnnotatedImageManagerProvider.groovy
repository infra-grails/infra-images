package infra.images

import groovy.transform.CompileStatic
import infra.file.storage.FileStorageService
import infra.file.storage.FilesHolder
import infra.file.storage.FilesManager
import infra.images.annotations.Format
import infra.images.annotations.Image
import infra.images.annotations.ImageHolder
import infra.images.domain.DomainImageManager
import infra.images.domain.ImageDomainRepoProvider
import infra.images.format.AnnotationFormat
import infra.images.format.BasesFormat
import infra.images.format.ImageFormat
import infra.images.formatter.ImageFormatter
import infra.images.util.ImageFormatsBundle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author alari
 * @since 12/23/12 4:06 PM
 */
@Component
@CompileStatic
class AnnotatedImageManagerProvider {
    @Autowired
    FileStorageService fileStorageService
    @Autowired
    ImageFormatter imageFormatter
    @Autowired
    ImageDomainRepoProvider imageDomainRepoProvider

    WeakHashMap<Class,Provider> providerWeakHashMap = [:]

    Provider getProvider(Class aClass) {
        providerWeakHashMap.get(aClass) ?: providerWeakHashMap.put(aClass, new Provider(aClass))
    }

    ImageManager getManager(def domain) {
        getProvider(domain.class).getManager(domain, imageFormatter, imageDomainRepoProvider, fileStorageService)
    }

    void clear() {
    }

    static private class Provider {
        private final ImageFormatsBundle imageBundle
        private final Class domainClass
        private final FilesHolder filesHolder

        private boolean storeDomains
        private String versionProperty

        Provider(Class aClass) {
            domainClass = aClass
            ImageHolder holder = domainClass.getAnnotation(ImageHolder)
            storeDomains = holder.enableImageDomains()
            versionProperty = holder.versionProperty() ?: null

            filesHolder = holder.filesHolder()

            Image imageAnnotation = holder.image()

            BasesFormat basesFormat = new BasesFormat(imageAnnotation.baseFormat())
            Map<String, ImageFormat> formats = [:]
            for (Format format : imageAnnotation.formats()) {
                formats.put(format.name(), new AnnotationFormat(format, basesFormat))
            }

            imageBundle = new ImageFormatsBundle(imageAnnotation.name(), formats, basesFormat)
        }

        // TODO: add cache
        ImageManager getManager(def domain, ImageFormatter imageFormatter, ImageDomainRepoProvider imageDomainRepoProvider, FileStorageService fileStorageService) {
            ImageManager m = new BasicImageManager(fileStorageService.getManager(domain, filesHolder as FilesHolder), imageBundle, imageFormatter)
            if (storeDomains) {
                m = new DomainImageManager(m, imageDomainRepoProvider)
            }
            if(versionProperty) {
                m = new VersionedImageManager(m, domain, versionProperty)
            }
            m
        }
    }
}
