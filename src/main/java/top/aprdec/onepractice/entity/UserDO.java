package top.aprdec.onepractice.entity;

import com.easy.query.core.annotation.*;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.context.annotation.Primary;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;
import top.aprdec.onepractice.entity.proxy.UserDOProxy;
import top.aprdec.onepractice.interceptor.AESEncryptionStrategy;

import java.security.Key;


@Data
@Table("user")
@EntityProxy
@FieldNameConstants
public class UserDO extends BaseDO implements ProxyEntityAvailable<UserDO , UserDOProxy> {
    @Column(primaryKey = true)
    private Long id;
    private String username;
    @Encryption(strategy = AESEncryptionStrategy.class)
    private String password;
    @Encryption(strategy = AESEncryptionStrategy.class)
    private String email;
    @UpdateIgnore
    @Column("user_type")
    private Integer usertype;
}
