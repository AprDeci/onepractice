package top.aprdec.onepractice.dto.req;

import lombok.Data;

@Data
public class EmailCaptchaReqDTO {
    private String email;
    private String code;
}
