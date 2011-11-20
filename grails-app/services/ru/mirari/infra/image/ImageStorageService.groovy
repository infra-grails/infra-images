package ru.mirari.infra.image

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import ru.mirari.infra.image.ImageHolder
import ru.mirari.infra.image.ImageFormat

/**
 * @author alari
 * @since 11/1/11 1:10 PM
 */
class ImageStorageService {
    static transactional = false

    def fileStorageService

    /**
     * Builds filename from format, type, and prefered filename
     *
     * @param format
     * @param filename
     * @return
     */
    private String getFilename(final ImageFormat format, final String filename = null) {
        (filename ?: format.name) + "." + format.type.toString()
    }

    /**
     * Stores already formatted image, given as a file
     *
     * @param format
     * @param image
     * @param path
     * @param filename
     * @param bucket
     */
    void storeFormatted(final ImageFormat format, final File image, String path, String filename = null,
                        String bucket = null) {
        filename = getFilename(format, filename)
        fileStorageService.store(image, path, filename, bucket)
    }

    /**
     * Stores already formatted image, given as a buffered image
     *
     * @param format
     * @param image
     * @param path
     * @param filename
     * @param bucket
     */
    void storeFormatted(final ImageFormat format, final BufferedImage image, String path, String filename = null,
                        String bucket = null) {
        File tmp = File.createTempFile(getClass().simpleName + format.name, "." + format.type.toString())
        ImageIO.write(image, format.type.toString(), tmp)
        storeFormatted(format, tmp, path, filename, bucket)
        tmp.delete()
    }

    /**
     * Formats and stores an image given as a file
     *
     * @param format
     * @param image
     * @param path
     * @param filename
     * @param bucket
     */
    void format(final ImageFormat format, final File image, String path, String filename = null,
                String bucket = null) {
        storeFormatted(format, format.formatToBuffer(image), path, filename, bucket)
    }

    /**
     * Formats and stores an image given as a buffer
     *
     * @param format
     * @param image
     * @param path
     * @param filename
     * @param bucket
     */
    void format(final ImageFormat format, final BufferedImage image, String path, String filename = null,
                String bucket = null) {
        storeFormatted(format, format.format(image), path, filename, bucket)
    }

    /**
     * Formats an image file into several instances for ImageHolder; stores all the ones
     * If you pass formats list, it only saves images in these formats
     *
     * @param holder
     * @param original
     * @param formats
     * @param deleteOriginal
     */
    void format(final ImageHolder holder, File original,
                List<ImageFormat> formats = [], boolean deleteOriginal = true) {
        formats = (formats ?: holder.imageFormats).sort()

        ImageFormat largest = formats.pop()
        BufferedImage im = largest.formatToBuffer(original)

        for (ImageFormat f in formats) {
            format(f, im, holder.imagesPath, null, holder.imagesBucket)
        }
        storeFormatted(largest, im, holder.imagesPath, null, holder.imagesBucket)

        if (deleteOriginal) {
            original.delete()
        }
    }

    /**
     * Formats an image file into several formats, stores them all, deletes original
     *
     * @param formats
     * @param original
     * @param path
     * @param bucket
     */
    void formatAndDelete(List<ImageFormat> formats, File original, String path, String bucket = null) {
        List<ImageFormat> imageFormats = formats.sort()

        ImageFormat largest = imageFormats.pop()
        BufferedImage im = largest.formatToBuffer(original)

        for (ImageFormat f in imageFormats) {
            format(f, im, path, null, bucket)
        }
        storeFormatted(largest, im, path, null, bucket)
        original.delete()
    }

    /**
     * Returns image absolute url
     *
     * @param format
     * @param path
     * @param filename
     * @param bucket
     * @return
     */
    String getUrl(ImageFormat format, String path, String filename = null, String bucket = null) {
        fileStorageService.getUrl(path, getFilename(format, filename), bucket)
    }

    /**
     * Returns image absolute url
     *
     * @param holder
     * @param format
     * @return
     */
    String getUrl(final ImageHolder holder, ImageFormat format = null) {
        fileStorageService.getUrl(holder.imagesPath, getFilename(format ?: holder.defaultImageFormat),
                holder.imagesBucket)
    }

    /**
     * Deletes specified image format or all the holder-attached images
     *
     * @param holder
     * @param format
     */
    void delete(final ImageHolder holder, ImageFormat format = null) {
        if (format) {
            fileStorageService.delete(holder.imagesPath, getFilename(format), holder.imagesBucket)
        } else {
            for (ImageFormat f in holder.imageFormats) {
                fileStorageService.delete(holder.imagesPath, getFilename(f), holder.imagesBucket)
            }
        }
    }
}
