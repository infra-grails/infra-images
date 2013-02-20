package infra.images.domain

import infra.images.ImageManager
import infra.images.format.ImageFormat
import infra.images.util.ImageBox
import infra.images.util.ImageInfo
import infra.images.util.ImageSize
import org.springframework.web.multipart.MultipartFile

/**
 * @author alari
 * @since 1/16/13 12:55 PM
 */
class DomainImageManager implements ImageManager {
    @Delegate private final ImageManager manager

    private final ImageDomainRepo imageDomainRepo

    DomainImageManager(ImageManager manager, ImageDomainRepoProvider imageDomainRepoProvider) {
        this.manager = manager
        imageDomainRepo = imageDomainRepoProvider.get(this.manager)
        this.manager.onStoreFile { ImageBox image, ImageFormat format ->
            imageDomainRepo.onStoreFile(image, format)
        }
    }

    @Override
    ImageSize getSize(ImageFormat format) {
        imageDomainRepo.getDomain(format)?.asSize() ?: format.size
    }

    @Override
    ImageInfo getInfo() {
        getInfo(formatsBundle.original)
    }

    @Override
    Map<String, ImageSize> store(File image) {
        imageDomainRepo.delete()
        manager.store(image)
    }

    @Override
    ImageInfo getInfo(String formatName) {
        if (!formatsBundle.formats.containsKey(formatName)) {
            throw new IllegalArgumentException()
        }
        getInfo(formatsBundle.formats.get(formatName))
    }

    @Override
    ImageInfo getInfo(ImageFormat format) {
        new ImageInfo(format, getSize(format), getSrc(format))
    }

    @Override
    Map<String, ImageSize> store(MultipartFile image) {
        imageDomainRepo.delete()
        manager.store(image)
    }

    @Override
    void delete() {
        imageDomainRepo.delete()
        manager.delete()
    }

    @Override
    ImageSize getSize(String formatName) {
        if (!formatsBundle.formats.containsKey(formatName)) {
            throw new IllegalArgumentException()
        }
        getSize(formatsBundle.formats.get(formatName))
    }

    @Override
    void removeFormat(String formatName) {
        if (!formatsBundle.formats.containsKey(formatName)) {
            throw new IllegalArgumentException()
        }
        removeFormat(formatsBundle.formats.get(formatName))
    }

    @Override
    void removeFormat(ImageFormat format) {
        imageDomainRepo.delete(format)
        manager.removeFormat(format)
    }

}
