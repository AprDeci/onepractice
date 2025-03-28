package top.aprdec.onepractice.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoRespDTO {
    String username;
    Integer userType;
}
