package annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EndpointDoc {
        String group();
        Method method();
        String url();
        String description() default "";
        Header[] headers() default {};
        StatusCode[] statusCodes() default {};
        String[] roles() default {"any"};
}