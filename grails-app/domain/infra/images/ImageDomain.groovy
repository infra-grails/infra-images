package infra.images

import infra.file.storage.FileDomain
import infra.images.util.ImageSize

class ImageDomain {
    int height
    int width
    float density

    static belongsTo = [file:FileDomain]

    static constraints = {
        file unique: true
    }

    ImageSize asSize() {
        ImageSize.buildReal(width, height, density)
    }

    void forSize(ImageSize size) {
        height = size.realHeight
        width = size.realWidth
        density = size.density
    }
}
