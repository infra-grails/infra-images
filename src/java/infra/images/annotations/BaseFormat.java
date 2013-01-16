package infra.images.annotations;

import infra.images.util.ImageCropPolicy;
import infra.images.util.ImageType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author alari
 * @since 12/21/12 4:33 PM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseFormat {
    ImageCropPolicy crop() default ImageCropPolicy.DEFAULT;

    ImageType type() default ImageType.DEFAULT;

    float quality() default -1;

    float density() default -1;
}
