Grails plugin: mirari-infra-image
====================

Processes image resize, crop, reformat with Thumbnailator Java library. Stores result with [this](https://github
.com/alari/mirari-infra-file "mirari-infra-file Grails plugin, stores files locally or on Amazon S3")

Installation
---------------------

Clone plugin sources (you may fork it at first, and that'll be great) and set sources directory in `BuildConfig
.groovy`:

    grails.plugin.location.'mirari-infra-image' = "../mirari-infra-image"

You will need valid S3 credentials to deploy the plugin.

And notice: you shouldn't call `grails install-plugin` or add direct dependency to `BuildConfig.groovy`!

Otherwise, you may download packaged version of the plugin and call

`grails install-plugin grails-mirari-infra-image.0.1.zip`

Usage
---------------------

You may implement `ImagesHolder` interface with your domains to ease work with images. It requires creating one or
several ImageFormat instances, e.g.:

    static final ImageFormat FORMAT_TINY = new ImageFormat("90*90", "im-tiny", ImageCropPolicy.CENTER, ImageType.PNG)

To format a file, use `ImageFormat.format()` method. The better way is to use `imageStorageService`,
which depends on [this](https://github.com/alari/mirari-infra-file "mirari-infra-file Grails plugin")