package top.aprdec.onepractice.dto.req;

import com.github.dozermapper.core.Mapping;
import lombok.Data;

@Data
public class UserRegistReqDTO {

    private String username;

    private String password;

    private String email;

    private Integer usertype;
}
