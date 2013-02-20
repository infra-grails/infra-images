package infra.images

import infra.images.format.ImageFormat
import infra.images.util.ImageSize
import org.springframework.web.multipart.MultipartFile

/**
 * @author alari
 * @since 2/20/13 10:07 AM
 */
class VersionedImageManager implements ImageManager {
    @Delegate ImageManager manager
    private final def holder
    private final String versionProperty

    VersionedImageManager(ImageManager manager, def holder, String versionProperty) {
        this.manager = manager
        this.holder = holder
        this.versionProperty = versionProperty

        this.manager.formatsBundle.version = holder[versionProperty]
    }

    @Override
    Map<String, ImageSize> store(File image) {
        manager.delete()
        incrementVersion()
        manager.store(image)
    }

    @Override
    Map<String, ImageSize> store(MultipartFile image) {
        manager.delete()
        incrementVersion()
        manager.store(image)
    }

    @Override
    void reformat(String formatName) {
        reformat()
    }

    @Override
    void reformat(ImageFormat format) {
        reformat()
    }

    @Override
    void reformat() {
        manager.delete()
        incrementVersion()
        manager.reformat()
    }

    private void incrementVersion() {
        if (!manager.formatsBundle.version) {
            manager.formatsBundle.version = 0
        }
        manager.formatsBundle.version++
        holder[versionProperty] = manager.formatsBundle.version
    }
}
