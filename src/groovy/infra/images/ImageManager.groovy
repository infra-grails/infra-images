package infra.images

import infra.file.storage.FilesManager
import infra.images.format.ImageFormat
import infra.images.util.ImageFormatsBundle
import infra.images.util.ImageSize

/**
 * @author alari
 * @since 12/23/12 4:00 PM
 */
interface ImageManager {
    Map<String,ImageSize> store(File image)

    String getSrc()

    String getSrc(String formatName)

    String getSrc(ImageFormat format)

    void delete()

    ImageSize getSize()

    ImageSize getSize(String formatName)

    ImageSize getSize(ImageFormat format)

    void onStoreFile(Closure callback)

    FilesManager getFilesManager()

    ImageFormatsBundle getFormatsBundle()
}
