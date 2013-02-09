package infra.images

import org.springframework.beans.factory.annotation.Autowired

class ImagesService {

    static transactional = false

    @Autowired
    AnnotatedImageManagerProvider annotatedImageManagerProvider

    ImageManager getImageManager(final holder) {
        annotatedImageManagerProvider.getManager(holder)
    }
}
