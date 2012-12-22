package infra.images

import grails.plugin.spock.IntegrationSpec
import org.springframework.core.io.ClassPathResource
import ru.mirari.infra.file.FilesHolder
import ru.mirari.infra.image.AnnotatedImageHolder
import ru.mirari.infra.image.annotations.BaseFormat
import ru.mirari.infra.image.annotations.Format
import ru.mirari.infra.image.annotations.Image
import ru.mirari.infra.image.annotations.ImageHolder
import ru.mirari.infra.image.format.CustomImageFormat
import ru.mirari.infra.image.format.ImageCropPolicy
import ru.mirari.infra.image.format.ImageFormat
import ru.mirari.infra.image.format.ImageType
import spock.lang.Shared
import spock.lang.Stepwise

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@Stepwise
class AnnotatedImageHolderSpec extends IntegrationSpec {

    @Shared AnnotatedImageHolder imageHolder
    @Shared Holder holder
    @Shared File imageFile

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
        Map<String, String> imagesData
        Collection<String> fileNames
    }

    def setup() {
        imageFile = new ClassPathResource("test.png", this.class).getFile();
    }

    def "we can build a holder"() {
        given:
        holder = new Holder()
        imageHolder = new AnnotatedImageHolder(holder)

        expect:
        holder != null
        imageHolder != null
    }

    def "we can upload an image"() {
        given: "we know the stored files paths"
        String localRoot = "web-app/f/storage/pth/im/"
        String webRoot = "/f/storage/pth/im/"

        File original = new File("${localRoot}_original.png")
        File  crop = new File("${localRoot}crop.png")
        File  fit = new File("${localRoot}fit.png")

        File custom

        ImageFormat customFormat = new CustomImageFormat(125, 125)

        expect: "at first files does not exists"
        !original.exists()
        !crop.exists()
        !fit.exists()

        when: "we try to store an image"
        imageHolder.store(imageFile)

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
        imageHolder.getSrc() == webRoot.concat(original.name)
        imageHolder.getSrc("crop") == webRoot.concat(crop.name)
        imageHolder.getSrc("fit") == webRoot.concat(fit.name)

        when: "calling to unexistent image format by name"
        imageHolder.getSrc("test")

        then: "illegal argument exceptions shows we're not correct"
        thrown(IllegalArgumentException)

        when: "calling to a custom format"
        String customSrc = imageHolder.getSrc(customFormat)
        custom = new File(localRoot + customFormat.name + ".png")

        then: "new file is created"
        custom.exists()
        customSrc == webRoot.concat(custom.name)
        imageHolder.getSrc(customFormat) == customSrc

        when: "we delete the holder"
        imageHolder.delete()

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
