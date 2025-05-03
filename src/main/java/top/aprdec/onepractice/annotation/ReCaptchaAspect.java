package top.aprdec.onepractice.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.Iinterface.ReuqireRecaptcha;
import top.aprdec.onepractice.dto.req.HasCaptchaToken;
import top.aprdec.onepractice.exception.CommonException;
import top.aprdec.onepractice.util.RecaptchaUtil;

@Aspect
@Component
@Slf4j
public class ReCaptchaAspect {

    @Autowired
    private RecaptchaUtil recaptchaUtil;

    @Before("@annotation(top.aprdec.onepractice.Iinterface.ReuqireRecaptcha)")
    public void checkReCaptcha(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        if(args.length> 0 && args[0] instanceof HasCaptchaToken hasCaptchaToken){
            String recaptchaToken = hasCaptchaToken.getRecaptchaToken();
            boolean valid = recaptchaUtil.verifyRecaptcha(recaptchaToken);
            if(!valid){
                String errorMessage = signature.getMethod().getAnnotation(ReuqireRecaptcha.class).errorMessage();
                throw new CommonException(errorMessage);
                //TODO 低得分用户校验邮箱验证码
            }
        }else{
            throw new CommonException("参数错误");
        }

    }
}
