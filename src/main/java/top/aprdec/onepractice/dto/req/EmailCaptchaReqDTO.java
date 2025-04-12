package top.aprdec.onepractice.dto.req;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailCaptchaReqDTO {
    @Email
    private String email;
    private String code;
}
