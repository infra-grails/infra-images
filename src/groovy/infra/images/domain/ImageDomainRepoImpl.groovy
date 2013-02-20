package infra.images.domain

import infra.file.storage.FileDomain
import infra.file.storage.domain.DomainFilesManager
import infra.images.ImageDomain
import infra.images.format.ImageFormat
import infra.images.util.ImageBox
import infra.images.util.ImageFormatsBundle

/**
 * @author alari
 * @since 2/18/13 7:19 PM
 */
class ImageDomainRepoImpl implements ImageDomainRepo {
    private final Map<String, ImageDomain> imageDomainMap = [:]

    private final DomainFilesManager filesManager
    private final ImageFormatsBundle formatsBundle

    ImageDomainRepoImpl(DomainFilesManager filesManager, ImageFormatsBundle formatsBundle) {
        this.filesManager = filesManager
        this.formatsBundle = formatsBundle
    }

    @Override
    void onStoreFile(ImageBox image, ImageFormat format) {

        FileDomain fileDomain = getFileDomain(formatsBundle.getFormatFilename(format))

        assert fileDomain

        ImageDomain imageDomain = (ImageDomain)ImageDomain.findOrSaveByFile(fileDomain)
        imageDomain.forSize(image.size)
        imageDomain.save(failOnError: true)
        imageDomainMap.put(formatsBundle.getFormatFilename(format), imageDomain)

        assert imageDomain.id
    }

    @Override
    void delete(ImageFormat format) {
        getDomain(format)?.delete()
        imageDomainMap.remove(formatsBundle.getFormatFilename(format))
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
        String filename = formatsBundle.getFormatFilename(format)
        if (!imageDomainMap.containsKey(filename)) {
            FileDomain fileDomain = getFileDomain(filename)
            if (fileDomain) {
                imageDomainMap.put(filename, getImageDomain(fileDomain))
            } else {
                imageDomainMap.put filename, null
            }
        }
        imageDomainMap.get(filename)
    }

    private FileDomain getFileDomain(String filename) {
        filesManager.exists(filename) ? (FileDomain)filesManager.getDomain(filename) : null
    }

    private ImageDomain getImageDomain(FileDomain domain) {
        ImageDomain.findByFile(domain)
    }
}
