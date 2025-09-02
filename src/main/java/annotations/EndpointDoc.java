package annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Objects;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EndpointDoc {
        String group();
        Method method();
        String url();
        String description() default "";
        RequestField[] request() default {};
        ResponseField[] response() default {};
        Header[] headers() default {};
        StatusCode[] statusCodes() default {};
        String contentType() default "application/json";
        String[] roles() default {"any"};
}