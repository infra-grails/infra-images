package infra.images

import grails.plugin.spock.IntegrationSpec
import org.springframework.core.io.ClassPathResource
import ru.mirari.infra.file.FilesHolder
import ru.mirari.infra.image.ImageManager
import ru.mirari.infra.image.annotations.BaseFormat
import ru.mirari.infra.image.annotations.Format
import ru.mirari.infra.image.annotations.Image
import ru.mirari.infra.image.annotations.ImageHolder
import ru.mirari.infra.image.format.CustomImageFormat
import ru.mirari.infra.image.format.ImageFormat
import ru.mirari.infra.image.util.ImageCropPolicy
import ru.mirari.infra.image.util.ImageType
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@Stepwise
@Ignore
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
    path = { "pth" }
    )
    )
    private class Holder {
    }

    def setupSpec() {
        imageFile = new ClassPathResource("test.png", this.class).getFile();
    }

    def "we can build a holder"() {
        given:
        holder = new Holder()
        imageManager = imagesService.getImageManager(holder)

        expect:
        holder != null
        imageManager != null
    }

    def "we can upload an image"() {
        given: "we know the stored files paths"
        String localRoot = "web-app/f/storage/pth/im/"
        String webRoot = "/f/storage/pth/im/"

        File original = new File("${localRoot}im.png")
        File crop = new File("${localRoot}crop.png")
        File fit = new File("${localRoot}fit.png")

        File custom

        ImageFormat customFormat = new CustomImageFormat(125, 125)

        expect: "at first files does not exists"
        !original.exists()
        !crop.exists()
        !fit.exists()

        when: "we try to store an image"
        imageManager.store(imageFile)

        then: "image files exist and are of correct sizes"
        // files...
        original.exists()
        crop.exists()
        fit.exists()

        // with correct sizes...
        assertImageSize(original, 1920, 1200)
        assertImageSize(crop, 200, 200)
        assertImageSize(fit, 192, 120)

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

        when: "we delete the holder"
        imageManager.delete()

        then: "files are deleted too"
        !original.exists()
        !crop.exists()
        !fit.exists()
        !custom.exists()
    }

    private assertImageSize(File image, int width, int height) {
        final BufferedImage bimg = ImageIO.read(image);
        assert bimg.width == width
        assert bimg.height == height
    }
}
