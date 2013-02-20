package infra.images.domain

import infra.file.storage.domain.DomainFilesManager
import infra.images.ImageManager

/**
 * @author alari
 * @since 2/18/13 7:49 PM
 */
class ImageDomainRepoProviderImpl implements ImageDomainRepoProvider {
    @Override
    ImageDomainRepo get(ImageManager manager) {
        new ImageDomainRepoImpl((DomainFilesManager)manager.filesManager, manager.formatsBundle)
    }
}
