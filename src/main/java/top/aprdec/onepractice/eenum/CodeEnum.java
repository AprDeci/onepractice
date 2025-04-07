package top.aprdec.onepractice.eenum;

import lombok.Getter;

@Getter
public enum CodeEnum {
    PASSWORD_ERROR(10001,"密码错误"),
    CAPTCHA_SEND_ERROR(10002,"验证码发送失败"),
    CAPTCHA_ERROR(10003,"验证码错误"),
    USER_NOT_EXIST(10004,"用户不存在"),
    NOTLOGIN_ERROR(10005,"用户未登录");


    private int code;
    private String msg;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
