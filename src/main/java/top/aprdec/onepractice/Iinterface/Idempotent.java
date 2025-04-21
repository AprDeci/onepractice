package top.aprdec.onepractice.Iinterface;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    int timeout() default 1;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    String keyPrefix() default "onepractice:idempotent:";

    String delimiter() default "|";

    String message() default "重复请求，请稍后重试";
}
