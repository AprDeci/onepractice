package top.aprdec.onepractice.util;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;
import top.aprdec.onepractice.Iinterface.Idempotent;
import top.aprdec.onepractice.Iinterface.RequestKeyParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestKeyGenerator {

    public static String getLockKey(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        final Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<parameters.length;i++){
            final RequestKeyParam keyParam = parameters[i].getAnnotation(RequestKeyParam.class);
            if(keyParam == null){
                continue;
            }
            sb.append(idempotent.delimiter()).append(args[i]);
        }
        if (StringUtils.isEmpty(sb.toString())) {
            //获取方法上的多个注解（为什么是两层数组：因为第二层数组是只有一个元素的数组）
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            //循环注解
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                //获取注解类中所有的属性字段
                final Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    //判断字段上是否有RequestKeyParam注解
                    final RequestKeyParam annotation = field.getAnnotation(RequestKeyParam.class);
                    //如果没有，跳过
                    if (annotation == null) {
                        continue;
                    }
                    //如果有，设置Accessible为true（为true时可以使用反射访问私有变量，否则不能访问私有变量）
                    field.setAccessible(true);
                    //如果属性是RequestKeyParam注解，则拼接 连接符" & + RequestKeyParam"
                    sb.append(idempotent.delimiter()).append(ReflectionUtils.getField(field, object));
                }
            }
        }
        //返回指定前缀的key
        return idempotent.keyPrefix() + sb;
    }
}
