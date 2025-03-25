package top.aprdec.onepractice.service;

public interface CaptchaService {
    void getEmailCaptcha(String email);

    Boolean checkEmailCaptcha(String email, String captcha);

}
