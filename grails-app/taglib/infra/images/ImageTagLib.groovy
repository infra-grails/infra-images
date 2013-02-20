package infra.images

import infra.images.format.CustomFormat

class ImageTagLib {
    static namespace = "infra"

    def imagesService

    def image = {attrs, body->
        def holder = attrs.remove("holder")

        ImageManager imageManager = imagesService.getImageManager(holder)
        if (!imageManager.isStored()) {
            out << body()
            return;
        }

        String format = attrs.remove("format")
        if (format) {
            out << imageManager.getInfo(format).getImg(attrs)
        } else {
            String w = attrs.remove("width")
            String h = attrs.remove("height")
            String d = attrs.remove("density")
            CustomFormat customFormat = new CustomFormat(w ? Integer.parseInt(w) : 0, h ? Integer.parseInt(h) : 0, d ? Float.parseFloat(d) : 1f)
            out << imageManager.getInfo(customFormat).getImg(attrs)
        }
    }
}
