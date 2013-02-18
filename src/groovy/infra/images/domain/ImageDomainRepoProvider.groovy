package infra.images.domain

import infra.images.ImageManager

/**
 * @author alari
 * @since 2/18/13 7:47 PM
 */
interface ImageDomainRepoProvider {
    ImageDomainRepo get(ImageManager manager)
}
