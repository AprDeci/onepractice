package top.aprdec.onepractice.entity;

import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;
import top.aprdec.onepractice.entity.proxy.WordsDOProxy;

/**
 * 实体类。
 *
 * @author Aprdec
 * @since 1.0
 */
@Data
@Table(value = "words")
@EntityProxy
public class WordsDO extends BaseDO implements ProxyEntityAvailable<WordsDO , WordsDOProxy> {

    @Column(primaryKey = true, value = "id")
    private Long id;

    private String word;

    /**
     * 全拼缩写
     */
    private String sw;


}
