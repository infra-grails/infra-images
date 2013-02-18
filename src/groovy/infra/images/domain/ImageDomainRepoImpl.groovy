package infra.images.domain

import infra.file.storage.FileDomain
import infra.file.storage.domain.DomainFilesManager
import infra.images.ImageDomain
import infra.images.format.ImageFormat
import infra.images.util.ImageBox

/**
 * @author alari
 * @since 2/18/13 7:19 PM
 */
class ImageDomainRepoImpl implements ImageDomainRepo {
    private final Map<String, ImageDomain> imageDomainMap = [:]

    private final DomainFilesManager filesManager

    ImageDomainRepoImpl(DomainFilesManager filesManager) {
        this.filesManager = filesManager
    }

    @Override
    void onStoreFile(ImageBox image, ImageFormat format) {

        FileDomain fileDomain = getFileDomain(format.filename)

        assert fileDomain

        ImageDomain imageDomain = (ImageDomain)ImageDomain.findOrSaveByFile(fileDomain)
        imageDomain.forSize(image.size)
        imageDomain.save(failOnError: true)
        imageDomainMap.put(format.filename, imageDomain)

        assert imageDomain.id
    }

    @Override
    void delete(ImageFormat format) {
        getDomain(format)?.delete()
        imageDomainMap.remove(format.filename)
    }

    @Override
    void delete() {
        filesManager.fileNames.each {
            getImageDomain(getFileDomain(it))?.delete(flush: true)
            imageDomainMap.remove(it)
        }
        imageDomainMap.clear()
    }

    @Override
    ImageDomain getDomain(ImageFormat format) {
        if (!imageDomainMap.containsKey(format.filename)) {
            FileDomain fileDomain = getFileDomain(format.filename)
            if (fileDomain) {
                imageDomainMap.put(format.filename, getImageDomain(fileDomain))
            } else {
                imageDomainMap.put format.filename, null
            }
        }
        imageDomainMap.get(format.filename)
    }

    private FileDomain getFileDomain(String filename) {
        filesManager.exists(filename) ? (FileDomain)filesManager.getDomain(filename) : null
    }

    private ImageDomain getImageDomain(FileDomain domain) {
        ImageDomain.findByFile(domain)
    }
}
