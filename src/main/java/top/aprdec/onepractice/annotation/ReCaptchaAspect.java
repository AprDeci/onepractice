package top.aprdec.onepractice.annotation;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.Iinterface.ReuqireRecaptcha;
import top.aprdec.onepractice.dto.req.HasCaptchaToken;
import top.aprdec.onepractice.dto.req.UserLoginReqDTO;
import top.aprdec.onepractice.eenum.ErrorEnum;
import top.aprdec.onepractice.entity.UserDO;
import top.aprdec.onepractice.entity.proxy.UserDOProxy;
import top.aprdec.onepractice.exception.CommonException;
import top.aprdec.onepractice.exception.GeneralBusinessException;
import top.aprdec.onepractice.service.CaptchaService;
import top.aprdec.onepractice.util.RecaptchaUtil;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ReCaptchaAspect {


    private final RecaptchaUtil recaptchaUtil;
    private final CaptchaService captchaService;
    private final EasyEntityQuery easyEntityQuery;


    @Before("@annotation(top.aprdec.onepractice.Iinterface.ReuqireRecaptcha)")
    public void checkReCaptcha(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        if(args.length> 0 && args[0] instanceof HasCaptchaToken hasCaptchaToken){
            String recaptchaToken = hasCaptchaToken.getRecaptchaToken();
            boolean valid = recaptchaUtil.verifyRecaptcha(recaptchaToken);
            if(!valid){
                ReuqireRecaptcha annotation = signature.getMethod().getAnnotation(ReuqireRecaptcha.class);
                if(annotation.sendEmailOnFailure()){
                    String email = getUserEmailFromRequest(args[0]);
                    if(email != null){
                        captchaService.getEmailCaptcha(email);
                    }
                }

                throw new GeneralBusinessException(ErrorEnum.RECAPTCHA_SCORE_LOW);

                //TODO 低得分用户校验邮箱验证码
            }
        }else{
            throw new GeneralBusinessException(ErrorEnum.RECAPTCHA_SCORE_LOW);
        }

    }

    //获取参数内容
    private String getUserEmailFromRequest(Object requestParam) {
        if (requestParam instanceof UserLoginReqDTO) {
            UserLoginReqDTO loginReq = (UserLoginReqDTO) requestParam;
            String usernameOrEmail = loginReq.getUsernameorEmail();

            // 检查是否是邮箱格式
            if (usernameOrEmail.contains("@")) {
                return usernameOrEmail.toLowerCase();
            }

            // 如果不是邮箱格式，尝试查询数据库获取邮箱
            UserDO user = easyEntityQuery.queryable(UserDO.class)
                    .where(u -> u.username().eq(usernameOrEmail))
                    .select(u->u.FETCHER.email())
                    .firstOrNull();

            return user != null ? user.getEmail() : null;
        }
        return null;
    }

}
