package top.aprdec.onepractice.entity.abstractDO;

import com.easy.query.core.annotation.UpdateIgnore;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public abstract class BaseDO {

    @UpdateIgnore
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
