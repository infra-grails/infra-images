package infra.images

import infra.file.storage.FilesManager
import infra.file.storage.LocalFileStorage
import infra.images.format.CustomFormat
import infra.images.format.ImageFormat
import infra.images.formatter.ImageFormatter
import infra.images.util.ImageBox
import infra.images.util.ImageFormatsBundle
import infra.images.util.ImageInfo
import infra.images.util.ImageSize
import org.springframework.web.multipart.MultipartFile

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.util.logging.Logger

/**
 * @author alari
 * @since 1/16/13 11:58 AM
 */
class BasicImageManager implements ImageManager {
    private final FilesManager filesManager
    private final ImageFormatsBundle imageBundle
    private final ImageFormatter imageFormatter
    private ImageBox originalImage
    private List<Closure> onStoreFileCallbacks = []

    private static final Logger log = Logger.getLogger(this.canonicalName)

    BasicImageManager(FilesManager filesManager, ImageFormatsBundle imageBundle, ImageFormatter imageFormatter) {
        this.filesManager = filesManager
        this.imageBundle = imageBundle
        this.imageFormatter = imageFormatter
    }

    @Override
    ImageFormatsBundle getFormatsBundle() {
        imageBundle
    }

    @Override
    ImageInfo getInfo() {
        getInfo(imageBundle.original)
    }

    @Override
    ImageInfo getInfo(String formatName) {
        getInfo(getFormat(formatName))
    }

    @Override
    ImageInfo getInfo(ImageFormat format) {
        new ImageInfo(format, getSize(format), getSrc(format))
    }

    @Override
    FilesManager getFilesManager() {
        filesManager
    }

    @Override
    Map<String, ImageSize> store(File image) {
        delete()
        originalImage = new ImageBox(image)
        Map<String, ImageSize> fileSizes = [:]

        // TODO: use GPars
        for (ImageFormat format in imageBundle.formats.values()) {
            ImageBox box = imageFormatter.format(format, originalImage)
            fileSizes.put(storeFile(box, format), box.size)
        }

        fileSizes
    }

    @Override
    Map<String, ImageSize> store(MultipartFile image) {
        if (!image || !image.size) {
            return null
        }
        File imageFile = File.createTempFile("infra-image", "store")
        imageFile.deleteOnExit()
        image.transferTo(imageFile)
        store(imageFile)
    }

    private String storeFile(ImageBox image, ImageFormat format) {
        filesManager.store(image.file, format.filename)
        for (Closure c in onStoreFileCallbacks) {
            c.call(image, format)
        }
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
        if (format instanceof CustomFormat) {
            format.setBaseFormat(imageBundle.basesFormat)
            if (!filesManager.exists(format.filename)) {
                loadOriginal()
                ImageBox box = imageFormatter.format(format, originalImage)
                storeFile(box, format)
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
        if (filesManager.storage instanceof LocalFileStorage) {
            if (filesManager.exists(format.filename)) {
                final BufferedImage bimg = ImageIO.read(filesManager.getFile(format.filename))
                if (bimg) {
                    return ImageSize.buildReal(bimg.width, bimg.height, format.density)
                } else return format.size
            }
            return format.size
        }
        getSizeBySrc(getSrc(format), format.density) ?: format.size
    }

    @Override
    void onStoreFile(Closure callback) {
        onStoreFileCallbacks.add(callback)
    }

    protected ImageFormat getFormat(String formatName) {
        if (!imageBundle.formats.containsKey(formatName)) {
            throw new IllegalArgumentException("Format ${formatName} is not defined by name")
        }
        imageBundle.formats.get(formatName)
    }

    private static ImageSize getSizeBySrc(String src, float density) {
        try {
            URL url = new URL(src)
            final BufferedImage bimg = ImageIO.read(url);
            if (bimg) {
                return ImageSize.buildReal(bimg.width, bimg.height, density)
            }

        } catch (Exception e) {
            log.info "Image file not found: ${src}"
        }
        ImageSize.buildReal(0, 0)
    }
}
