package infra.images.domain

import infra.images.ImageDomain
import infra.images.format.ImageFormat
import infra.images.util.ImageBox

/**
 * @author alari
 * @since 2/18/13 7:19 PM
 */
public interface ImageDomainRepo {
    void onStoreFile(ImageBox image, ImageFormat format)

    void delete(ImageFormat format)

    void delete()

    ImageDomain getDomain(ImageFormat format)
}