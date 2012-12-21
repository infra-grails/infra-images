package ru.mirari.infra.image.annotations;

import ru.mirari.infra.image.format.ImageCropPolicy;
import ru.mirari.infra.image.format.ImageType;

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

    float dexterity() default -1;
}
