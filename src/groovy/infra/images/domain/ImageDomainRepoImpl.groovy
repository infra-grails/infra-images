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

        assert imageDomain.id
    }

    @Override
    void delete(ImageFormat format) {
        getDomain(format)?.delete(flush: true)
    }

    @Override
    void delete() {
        filesManager.refresh()
        filesManager.fileNames.each {
            getImageDomain(getFileDomain(it))?.delete(flush: true)
        }
    }

    @Override
    ImageDomain getDomain(ImageFormat format) {
        String filename = formatsBundle.getFormatFilename(format)
        FileDomain fileDomain = getFileDomain(filename)
        fileDomain ? getImageDomain(fileDomain) : null
    }

    private FileDomain getFileDomain(String filename) {
        filesManager.exists(filename) ? (FileDomain)filesManager.getDomain(filename) : null
    }

    private ImageDomain getImageDomain(FileDomain domain) {
        ImageDomain.findByFile(domain)
    }
}
