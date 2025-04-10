package top.aprdec.onepractice.eenum;

import lombok.Getter;

@Getter
public enum ErrorEnum {
//    10000-20000 用户错误
    PASSWORD_ERROR(10001,"密码错误"),
    CAPTCHA_ERROR(10003,"验证码错误"),
    USER_NOT_EXIST(10004,"用户不存在"),
    TOKEN_INVALID(10005,"Token失效"),
    REPEAT_LOGIN(10006,"重复登录"),
    REPEAT_OPERATION(10007,"重复操作"),
    EMAIL_SEND_WAIT(10008,"邮箱已经发送 稍后再试"),
    PASSWORD_OR_USER_ERROR(10009,"密码错误或用户不存在"),


//    20000-30000 系统错误
    CAPTCHA_SEND_ERROR(20001,"验证码发送失败"),
    REGISTER_ERROR(20002,"注册失败"),
    OPERATE_ERROR(20003,"操作失败"),



//    30000-40000 数据错误
    USERNAME_EXIST(30001,"用户名已存在"),


    //40000-50000 权限错误


    //50000-60000 参数错误

    PARAM_IS_INVALID(60001, "参数无效"),
    PARAM_IS_BLANK(60002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(60003, "参数格式错误"),
    PARAM_NOT_COMPLETE(60004, "参数缺失");


    private int code;
    private String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
