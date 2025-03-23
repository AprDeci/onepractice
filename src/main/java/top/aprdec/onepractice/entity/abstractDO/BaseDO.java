package top.aprdec.onepractice.entity.abstractDO;

import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.UpdateIgnore;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import top.aprdec.onepractice.entity.abstractDO.proxy.BaseDOProxy;

import java.time.LocalDateTime;
@Data
public abstract class BaseDO {
    @Column(primaryKey = true)
    private Long id;

    @UpdateIgnore
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
