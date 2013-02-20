package infra.images

import grails.plugin.spock.IntegrationSpec
import infra.file.storage.FilesHolder
import infra.images.annotations.BaseFormat
import infra.images.annotations.Format
import infra.images.annotations.Image
import infra.images.annotations.ImageHolder
import infra.images.domain.DomainImageManager
import infra.images.format.CustomFormat
import infra.images.format.ImageFormat
import infra.images.util.ImageCropPolicy
import infra.images.util.ImageSize
import infra.images.util.ImageType
import org.springframework.core.io.ClassPathResource
import spock.lang.Shared
import spock.lang.Stepwise

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@Stepwise
class VersionedSpec extends IntegrationSpec {

    @Shared ImageManager imageManager
    @Shared File imageFile

    def imagesService

    @ImageHolder(
            image = @Image(name = "im",
                    baseFormat = @BaseFormat(type = ImageType.PNG),
                    formats = [
                    @Format(name = "fit", crop = ImageCropPolicy.NONE, width = 192, height = 192)
                    ]),
            filesHolder = @FilesHolder(
                    path = { "vers/v" },
                    enableFileDomains = true
            ),
            enableImageDomains = true,
            versionProperty = "version"
    )
    private class VersionedHolder {
        Integer version
    }

    def setupSpec() {
        new File("web-app/f").deleteDir()
        imageFile = new ClassPathResource("test.png", this.class).getFile();
    }

    def cleanup() {
        new File("web-app/f").deleteDir()
        imagesService.annotatedImageManagerProvider.clear()
    }

    def "versioned spec"() {
        def holder = new VersionedHolder()
        imageManager = imagesService.getImageManager(holder)

        String localRoot = "web-app/f/storage/vers/v/"

        File original = new File("${localRoot}im.1.png")
        File fit = new File("${localRoot}fit.1.png")
        File originalUpdated = new File("${localRoot}im.2.png")
        File fitUpdated = new File("${localRoot}fit.2.png")
        File originalReformatted = new File("${localRoot}im.3.png")
        File fitReformatted = new File("${localRoot}fit.3.png")

        expect: "at first files does not exists"
        !original.exists()
        !fit.exists()
        !originalUpdated.exists()
        !fitUpdated.exists()
        holder.version == null

        when: "we store an image"
        imageManager.store(imageFile)

        then: "images are stored and versioned"
        holder.version == 1
        original.exists()
        fit.exists()

        when: "we update the image"
        imageManager.store(imageFile)

        then: "version is updated"
        holder.version == 2
        !original.exists()
        !fit.exists()
        originalUpdated.exists()
        fitUpdated.exists()

        when: "reformatting image"
        imageManager.reformat("fit")

        then: "image version incremented"
        holder.version == 3
        !originalUpdated.exists()
        !fitUpdated.exists()
        originalReformatted.exists()
        fitReformatted.exists()

        when: "removing format"
        imageManager.removeFormat("fit")

        then: "version is not changed"
        holder.version == 3
        originalReformatted.exists()
        !fitReformatted.exists()
    }
}
