package top.aprdec.onepractice.dto.req;

import lombok.Data;

@Data
public class ResetPasswordReqDTO {
    private String email;
    private String password;
}
