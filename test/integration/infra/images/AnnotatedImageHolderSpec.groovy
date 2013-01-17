package infra.images

import grails.plugin.spock.IntegrationSpec
import infra.images.util.ImageSize
import org.springframework.core.io.ClassPathResource
import infra.file.storage.FilesHolder
import infra.images.annotations.BaseFormat
import infra.images.annotations.Format
import infra.images.annotations.Image
import infra.images.annotations.ImageHolder
import infra.images.format.CustomFormat
import infra.images.format.ImageFormat
import infra.images.util.ImageCropPolicy
import infra.images.util.ImageType
import spock.lang.Shared
import spock.lang.Stepwise

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@Stepwise
class AnnotatedImageHolderSpec extends IntegrationSpec {

    @Shared ImageManager imageManager
    @Shared Holder holder
    @Shared File imageFile

    def imagesService

    @ImageHolder(
    image = @Image(name = "im",
    baseFormat = @BaseFormat(type = ImageType.PNG),
    formats = [
    @Format(name = "crop", crop = ImageCropPolicy.CENTER, width = 100, height = 100, density = 2f),
    @Format(name = "fit", crop = ImageCropPolicy.NONE, width = 192, height = 192)
    ]),
    filesHolder = @FilesHolder(
    path = { "pth/im" }
    )
    )
    private class Holder {
    }

    def setupSpec() {
        new File("web-app/f").deleteDir()
        imageFile = new ClassPathResource("test.png", this.class).getFile();
    }

    def cleanup() {
        new File("web-app/f").deleteDir()
    }

    def "we can upload an image"(boolean withDomains, Class managerClass) {
        given: "we know the stored files paths"

        holder = new Holder()
        imagesService.annotatedImageManagerProvider.clear()
        imageManager = imagesService.getImageManager(holder, withDomains)

        String localRoot = "web-app/f/storage/pth/im/"
        String webRoot = "/f/storage/pth/im/"

        File original = new File("${localRoot}im.png")
        File crop = new File("${localRoot}crop.png")
        File fit = new File("${localRoot}fit.png")

        File custom

        ImageFormat customFormat = new CustomFormat(192, 0, 1.5f)

        println withDomains

        expect: "at first files does not exists"
        imageManager.class == managerClass

        !original.exists()
        !crop.exists()
        !fit.exists()

        when: "we try to store an image"
        println imageManager.store(imageFile)

        then: "image files exist and are of correct sizes"
        // files...
        original.exists()
        crop.exists()
        fit.exists()

        // with correct sizes...
        assertImageSize(original, 1920, 1200, imageManager.getSize())
        assertImageSize(crop, 200, 200, imageManager.getSize("crop"))
        assertImageSize(fit, 192, 120, imageManager.getSize("fit"))

        // and correct urls
        imageManager.getSrc() == webRoot.concat(original.name)
        imageManager.getSrc("crop") == webRoot.concat(crop.name)
        imageManager.getSrc("fit") == webRoot.concat(fit.name)

        when: "calling to unexistent image format by name"
        imageManager.getSrc("test")

        then: "illegal argument exceptions shows we're not correct"
        thrown(IllegalArgumentException)

        when: "calling to a custom format"
        String customSrc = imageManager.getSrc(customFormat)
        custom = new File(localRoot + customFormat.name + ".png")

        then: "new file is created"
        custom.exists()
        customSrc == webRoot.concat(custom.name)
        imageManager.getSrc(customFormat) == customSrc
        assertImageSize(custom, 288, 180, imageManager.getSize(customFormat))

        when: "we delete the holder"
        imageManager.delete()

        then: "files are deleted too"
        !original.exists()
        !crop.exists()
        !fit.exists()
        !custom.exists()

        where:
        withDomains | managerClass
        true | DomainImageManager
        false | BasicImageManager
    }

    private void assertImageSize(File image, int width, int height, ImageSize size) {
        final BufferedImage bimg = ImageIO.read(image);
        assert bimg.width == width
        assert bimg.height == height
        assert size.realWidth == width
        assert size.realHeight == height
    }
}
