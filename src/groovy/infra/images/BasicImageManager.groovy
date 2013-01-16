package infra.images

import infra.file.storage.FilesManager
import infra.images.format.CustomFormat
import infra.images.format.ImageFormat
import infra.images.formatter.ImageFormatter
import infra.images.util.ImageBox
import infra.images.util.ImageFormatsBundle
import infra.images.util.ImageSize

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

/**
 * @author alari
 * @since 1/16/13 11:58 AM
 */
class BasicImageManager implements ImageManager {
    private final FilesManager filesManager
    private final ImageFormatsBundle imageBundle
    private final ImageFormatter imageFormatter
    private ImageBox originalImage

    BasicImageManager(FilesManager filesManager, ImageFormatsBundle imageBundle, ImageFormatter imageFormatter) {
        this.filesManager = filesManager
        this.imageBundle = imageBundle
        this.imageFormatter = imageFormatter
    }

    @Override
    Map<String,ImageSize> store(File image) {
        delete()
        originalImage = new ImageBox(image)
        Map<String,ImageSize> fileSizes = [:]

        // TODO: use GPars
        for(ImageFormat format in imageBundle.formats.values()) {
            ImageBox box = imageFormatter.format(format, originalImage)
            fileSizes.put(filesManager.store(box.file, format.filename), box.size)
        }

        fileSizes
    }

    private void loadOriginal() {
        originalImage = new ImageBox(filesManager.getFile(imageBundle.original.filename))
    }

    @Override
    String getSrc() {
        getExistentFormatSrc(imageBundle.original)
    }

    @Override
    String getSrc(String formatName) {
        getExistentFormatSrc(getFormat(formatName))
    }

    @Override
    String getSrc(ImageFormat format) {
        if(format instanceof CustomFormat) {
            format.setBaseFormat(imageBundle.basesFormat)
            if (!filesManager.exists(format.filename)) {
                loadOriginal()
                ImageBox box = imageFormatter.format(format, originalImage)
                // TODO: leave callback for cache
                filesManager.store(box.file, format.filename)
            }
        }
        filesManager.getUrl(format.filename)
    }

    private String getExistentFormatSrc(ImageFormat format) {
        filesManager.getUrl(format.filename)
    }

    @Override
    void delete() {
        filesManager.delete()
    }

    @Override
    ImageSize getSize() {
        getSize(imageBundle.original)
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
        if (!imageBundle.formats.containsKey(formatName)) {
            throw new IllegalArgumentException("Format ${formatName} is not defined by name")
        }
        imageBundle.formats.get(formatName)
    }

    private ImageSize getSizeBySrc(String src, float density) {
        URL url = new URL(src)
        final BufferedImage bimg = ImageIO.read(url);
        if (bimg) {
            return ImageSize.buildReal(bimg.width, bimg.height, density)
        }
        null
    }
}
