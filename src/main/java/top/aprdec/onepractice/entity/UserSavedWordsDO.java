package top.aprdec.onepractice.entity;

import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;
import top.aprdec.onepractice.entity.proxy.UserSavedWordsDOProxy;

/**
 * 实体类。
 *
 * @author Aprdec
 * @since 1.0
 */
@Data
@Table(value = "user_saved_words")
@EntityProxy
public class UserSavedWordsDO extends BaseDO implements ProxyEntityAvailable<UserSavedWordsDO , UserSavedWordsDOProxy> {

    @Column(primaryKey = true, value = "id")
    private Long id;

    private Long wordId;

    private Long userId;

    /**
     * 在哪张卷子存储
     */
    private Integer paperId;


}
