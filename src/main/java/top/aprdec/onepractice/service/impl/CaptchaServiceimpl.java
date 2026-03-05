package top.aprdec.onepractice.service.impl;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.eenum.EmailTemplateEnum;
import top.aprdec.onepractice.eenum.ErrorEnum;
import top.aprdec.onepractice.entity.UserDO;
import top.aprdec.onepractice.exception.GeneralBusinessException;
import top.aprdec.onepractice.service.CaptchaService;
import top.aprdec.onepractice.util.EmailUtil;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

import static top.aprdec.onepractice.eenum.EmailTemplateEnum.VERIFICATION_CODE_EMAIL_SUBTITLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaptchaServiceimpl implements CaptchaService {
    private static final String CAPTCHA_SENDING_PLACEHOLDER = "SENDING";
    private final EmailUtil emailutil;
    private final RedisUtil redisutil;
    private final EasyEntityQuery easyEntityQuery;

    @Override
    public Boolean getEmailCaptcha(String email) {
        if(StringUtils.isBlank(email)){
            throw new GeneralBusinessException(ErrorEnum.PARAM_IS_BLANK);
        }
        email = email.toLowerCase();
        String key = RedisKeyConstant.CAPTCHA_EMAIL + email;

        // 原子占位，避免并发下重复发送
        Boolean occupied = redisutil.setNx(key, CAPTCHA_SENDING_PLACEHOLDER, 60);
        if (Boolean.FALSE.equals(occupied)) {
            throw new GeneralBusinessException(ErrorEnum.EMAIL_SEND_WAIT);
        }

        String captcha = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        log.info("email:{},captcha:{}", email, captcha);

        // 异步发送邮件，不等待结果
        String finalEmail = email;
        CompletableFuture.runAsync(() -> {
            try {
                emailutil.sendEmail(finalEmail,
                        EmailTemplateEnum.VERIFICATION_CODE_EMAIL_HTML.set(captcha),
                        VERIFICATION_CODE_EMAIL_SUBTITLE.getTemplate());
                // 发送成功，写入真实验证码
                redisutil.set(key, captcha, 60);
                log.info("验证码发送成功并已存入Redis");
            } catch (Exception e) {
                // 发送失败释放占位，避免用户一直被限流
                redisutil.del(key);
                log.error("验证码发送失败: {}", e.getMessage());
            }
        });
        // 不再等待发送结果
        return true;
    }

    @Override
    public Boolean checkEmailCaptcha(String email, String captcha) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        email = email.toLowerCase();
        String key = RedisKeyConstant.CAPTCHA_EMAIL+email;
        String cacheCaptcha = redisutil.getString(key);
        if(!StringUtils.isBlank(cacheCaptcha)) {
            Boolean result = StringUtils.equals(captcha, cacheCaptcha);
            if(result){
                redisutil.del(key);
            }
            return result;
        }else{
            return false;
        }
    }

    @Override
    public String checkEmailCaptchawhenResetPassword(String email, String captcha) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(captcha)) {
            throw new GeneralBusinessException(ErrorEnum.PARAM_IS_INVALID);
        }
        email = email.toLowerCase();
//        检查是否存在邮箱
        String finalEmail = email;
        long count = easyEntityQuery.queryable(UserDO.class).where(u -> u.email().eq(finalEmail)).count();
        if(count==0){
            throw new GeneralBusinessException(ErrorEnum.PARAM_IS_INVALID);
        }
        Boolean verified = checkEmailCaptcha(email, captcha);
        if (Boolean.FALSE.equals(verified)) {
            throw new GeneralBusinessException(ErrorEnum.CAPTCHA_ERROR);
        }
        String resetToken = UUID.randomUUID().toString().replace("-", "");
        redisutil.set(RedisKeyConstant.RESET_PASSWORD_TOKEN + resetToken, email, 300);
        return resetToken;
    }

}
