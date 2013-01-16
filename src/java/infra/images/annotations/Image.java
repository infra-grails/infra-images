package infra.images.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author alari
 * @since 12/21/12 4:33 PM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Image {
    BaseFormat baseFormat() default @BaseFormat;

    String name();

    Format[] formats() default {};
}
