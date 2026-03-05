package top.aprdec.onepractice.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordTokenRespDTO {
    private String resetToken;
}
