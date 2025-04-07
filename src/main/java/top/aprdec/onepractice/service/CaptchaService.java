package top.aprdec.onepractice.service;

public interface CaptchaService {
    Boolean getEmailCaptcha(String email);

    Boolean checkEmailCaptcha(String email, String captcha);

    Boolean checkEmailCaptchawhenResetPassword(String email, String captcha);
}
