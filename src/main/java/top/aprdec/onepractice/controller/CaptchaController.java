package top.aprdec.onepractice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.Iinterface.Idempotent;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.EmailCaptchaReqDTO;
import top.aprdec.onepractice.dto.resp.ResetPasswordTokenRespDTO;

import top.aprdec.onepractice.eenum.ErrorEnum;
import top.aprdec.onepractice.service.CaptchaService;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
@Validated
public class CaptchaController {
    private final CaptchaService captchaService;

    @Idempotent(timeout = 50,message = "验证码已发送，请稍后重试")
    @GetMapping("/email")
    public AResult<Void> getEmailCaptcha(@RequestParam @Email(message = "邮箱格式不正确") String email) {
        Boolean result = captchaService.getEmailCaptcha(email);
        if(result) return AResult.success();
        else return AResult.error(ErrorEnum.CAPTCHA_SEND_ERROR);
    }

    @PostMapping("/email/verify")
    public AResult<ResetPasswordTokenRespDTO> verifyEmailCaptcha(@RequestBody @Valid EmailCaptchaReqDTO dto) {
        String resetToken = captchaService.checkEmailCaptchawhenResetPassword(dto.getEmail(), dto.getCode());
        return AResult.success(new ResetPasswordTokenRespDTO(resetToken));
    }

}
