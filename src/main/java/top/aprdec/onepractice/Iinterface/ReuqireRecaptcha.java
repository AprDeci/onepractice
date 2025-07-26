package top.aprdec.onepractice.Iinterface;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReuqireRecaptcha {
    double minScore() default 0.5;

    String errorMessage() default "reCAPTCHA验证失败";

    boolean sendEmailOnFailure() default true;
}
