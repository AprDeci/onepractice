package top.aprdec.onepractice.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRespDTO {
    private Long id;
    private String username;
    private String password;
    private String token;
}
