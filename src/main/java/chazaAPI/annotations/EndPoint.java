package chazaAPI.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EndPoint {
        String group() default "";
        Method method();
        String url();
        String accept() default "";
        String description() default "";
        Field[] requestFields() default {};
        Field[] responseFields() default {};
        Class<?> requestDTO() default void.class;
        Class<?> responseDTO() default void.class;
        Header[] headers() default {};
        Status[] statusCodes() default {};
        String contentType() default "";
        String[] roles() default {};
}