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
import org.apache.log4j.Logger
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

    static final private Logger log = Logger.getLogger(AnnotatedImageManagerProvider);

    private volatile WeakHashMap<Class,Provider> providerWeakHashMap = [:]

    Provider getProvider(Class aClass) {
        Provider provider = providerWeakHashMap.get(aClass)
        if(!provider) {
            synchronized (this) {
                provider = new Provider(aClass)
                log.info "New AnnotatedImageManagerProvider for ${aClass}"
                providerWeakHashMap.put(aClass, provider)
            }
        }
        provider
    }

    ImageManager getManager(def domain) {
        assert domain.class
        assert getProvider(domain.class)
        getProvider(domain.class)
                .getManager(domain, imageFormatter, imageDomainRepoProvider, fileStorageService)
    }

    void clear() {
    }

    static private class Provider {
        private final ImageFormatsBundle imageBundle
        private final Class domainClass
        private final FilesHolder filesHolder

        private final boolean storeDomains
        private final String versionProperty

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
