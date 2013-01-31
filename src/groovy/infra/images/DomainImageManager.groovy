package infra.images

import infra.file.storage.DomainFilesManager
import infra.file.storage.FileDomain
import infra.images.format.ImageFormat
import infra.images.util.ImageBox
import infra.images.util.ImageFormatsBundle
import infra.images.util.ImageInfo
import infra.images.util.ImageSize
import org.springframework.web.multipart.MultipartFile

/**
 * @author alari
 * @since 1/16/13 12:55 PM
 */
class DomainImageManager implements ImageManager {
    private final ImageManager manager
    private final Map<String, ImageDomain> imageDomainMap = [:]

    DomainImageManager(ImageManager manager) {
        if (!manager.filesManager instanceof DomainFilesManager) {
            throw new IllegalArgumentException("To use DomainImageManager you have to use DomainFilesManager; but instance of ${manager.class.name} given")
        }
        this.manager = manager
        this.manager.onStoreFile { ImageBox image, ImageFormat format ->
            FileDomain fileDomain = filesManager.getDomain(format.filename)

            assert fileDomain

            ImageDomain imageDomain = ImageDomain.findOrSaveByFile(fileDomain)
            imageDomain.forSize(image.size)
            imageDomain.save(failOnError: true)
            imageDomainMap.put(format.filename, imageDomain)

            assert imageDomain.id
        }
    }

    @Override
    void onStoreFile(Closure callback) {
        manager.onStoreFile callback
    }

    @Override
    ImageSize getSize(ImageFormat format) {
        FileDomain fileDomain = filesManager.getDomain(format.filename)
        if (fileDomain) {
            ImageDomain imageDomain = ImageDomain.findByFile(fileDomain)
            if (imageDomain) {
                return imageDomain.asSize()
            }
        }
        return format.size
    }

    ImageDomain getDomain(String filename) {
        if (!imageDomainMap.containsKey(filename)) {
            if (filesManager.exists(filename)) {
                imageDomainMap.put(filename, ImageDomain.findByFile(filesManager.getDomain(filename)))
            } else {
                imageDomainMap.put filename, null
            }
        }
        imageDomainMap.get(filename)
    }

    @Override
    DomainFilesManager getFilesManager() {
        (DomainFilesManager) manager.filesManager
    }

    @Override
    ImageInfo getInfo() {
        getInfo(formatsBundle.original)
    }

    Map<String, ImageSize> store(File image) {
        filesManager.fileNames.each {
            getDomain(it)?.delete(flush: true)
            imageDomainMap.remove(it)
        }
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
        new ImageInfo(format, getSize(format), filesManager.getDomain(format.filename) ? getSrc(format) : "")
    }

    @Override
    Map<String, ImageSize> store(MultipartFile image) {
        filesManager.fileNames.each {
            getDomain(it)?.delete(flush: true)
            imageDomainMap.remove(it)
        }
        manager.store(image)
    }

    @Override
    void delete() {
        filesManager.fileNames.each {
            ImageDomain.findByFile(filesManager.getDomain(it))?.delete(flush: true)
        }
        manager.delete()
    }

    @Override
    ImageSize getSize(String formatName) {
        if (!formatsBundle.formats.containsKey(formatName)) {
            throw new IllegalArgumentException()
        }
        getSize(formatsBundle.formats.get(formatName))
    }
    //
    //      DELEGATING
    //


    @Override
    String getSrc() {
        manager.getSrc()
    }

    @Override
    String getSrc(String formatName) {
        manager.getSrc(formatName)
    }

    @Override
    String getSrc(ImageFormat format) {
        manager.getSrc(format)
    }


    @Override
    ImageSize getSize() {
        getSize(formatsBundle.original)
    }

    @Override
    ImageFormatsBundle getFormatsBundle() {
        manager.formatsBundle
    }
}
