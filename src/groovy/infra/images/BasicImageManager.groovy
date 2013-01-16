package infra.images

import infra.file.storage.FilesManager
import infra.images.format.ImageFormat
import infra.images.formatter.ImageFormatter
import infra.images.util.ImageBox
import infra.images.util.ImageFormatsBundle
import infra.images.util.ImageSize

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * @author alari
 * @since 1/16/13 11:58 AM
 */
class BasicImageManager implements ImageManager {
    private final FilesManager filesManager
    private final ImageFormatsBundle imageBundle
    private final ImageFormatter imageFormatter

    BasicImageManager(FilesManager filesManager, ImageFormatsBundle imageBundle, ImageFormatter imageFormatter) {
        this.filesManager = filesManager
        this.imageBundle = imageBundle
        this.imageFormatter = imageFormatter
    }

    @Override
    Map<String,ImageSize> store(File image) {
        delete()
        ImageBox original = new ImageBox(image)
        Map<String,ImageSize> fileSizes = [:]

        // TODO: use GPars
        for(ImageFormat format in imageBundle.formats.values()) {
            ImageBox box = imageFormatter.format(format, original)
            fileSizes.put(filesManager.store(box.file, format.filename), box.size)
        }

        fileSizes
    }

    @Override
    String getSrc() {
        filesManager.getUrl(imageBundle.original)
    }

    @Override
    String getSrc(String formatName) {
        getSrc(getFormat(formatName))
    }

    @Override
    String getSrc(ImageFormat format) {
        filesManager.getUrl(format.filename)
    }

    @Override
    void delete() {
        filesManager.delete()
    }

    @Override
    ImageSize getSize() {
        getSizeBySrc(imageBundle.original)
    }

    @Override
    ImageSize getSize(String formatName) {
        getSize(getFormat(formatName))
    }

    @Override
    ImageSize getSize(ImageFormat format) {
        getSizeBySrc(getSrc(format), format.density)
    }

    protected ImageFormat getFormat(String formatName) {
        imageBundle.formats.get(formatName)
    }

    private ImageSize getSizeBySrc(String src, float density) {
        URL url = new URL(src)
        final BufferedImage bimg = ImageIO.read(url);
        if (bimg) {
            return new ImageSize(bimg.width, bimg.height, density)
        }
        null
    }
}
