package top.aprdec.onepractice.Iinterface;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequestKeyParam {
}
