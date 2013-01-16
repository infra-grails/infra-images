package infra.images.annotations;

import infra.file.storage.FilesHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author alari
 * @since 12/18/12 10:40 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ImagesHolder {

    Image[] images();

    BaseFormat baseFormat() default @BaseFormat;

    FilesHolder filesHolder();
}




