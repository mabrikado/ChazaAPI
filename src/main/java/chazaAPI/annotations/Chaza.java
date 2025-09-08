package chazaAPI.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Chaza {
    String group() default "/";
    String description() default "";
    String baseUrl() default "";
    String accept() default "text/plain";
    String contentType() default "text/plain";
    String[] roles() default {"any"};
}
