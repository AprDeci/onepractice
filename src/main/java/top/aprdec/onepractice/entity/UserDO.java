package top.aprdec.onepractice.entity;

import com.easy.query.core.annotation.*;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;
import top.aprdec.onepractice.entity.proxy.UserDOProxy;


@Data
@Table("user")
@EntityProxy
@FieldNameConstants
public class UserDO extends BaseDO implements ProxyEntityAvailable<UserDO , UserDOProxy> {
    private String username;
    private String password;
    private String email;
    @UpdateIgnore
    @Column("user_type")
    private Integer usertype;
}
