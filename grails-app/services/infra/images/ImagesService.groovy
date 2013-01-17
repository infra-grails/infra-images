package infra.images

import infra.images.AnnotatedImageManagerProvider
import infra.images.ImageManager
import org.springframework.beans.factory.annotation.Autowired

class ImagesService {

    static transactional = false

    @Autowired
    AnnotatedImageManagerProvider annotatedImageManagerProvider

    ImageManager getImageManager(final holder, boolean imageDomains = false) {
        annotatedImageManagerProvider.getManager(holder, imageDomains)
    }
}
