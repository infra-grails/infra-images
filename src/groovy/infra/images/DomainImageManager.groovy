package infra.images

/**
 * @author alari
 * @since 1/16/13 12:55 PM
 */
class DomainImageManager implements ImageManager {
    @Delegate
    private final ImageManager manager

    DomainImageManager(ImageManager manager) {
        this.manager = manager
    }
}
