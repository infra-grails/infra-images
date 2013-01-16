package infra.images

import infra.file.storage.FileDomain
import infra.images.util.ImageSize

class ImageDomain {
    int height
    int width
    float density

    FileDomain file

    static belongsTo = FileDomain

    static constraints = {
        file unique: true
    }

    ImageSize asSize() {
        new ImageSize(width, height, density)
    }

    void forSize(ImageSize size) {
        height = size.realHeight
        width = size.realWidth
        density = size.density
    }
}
