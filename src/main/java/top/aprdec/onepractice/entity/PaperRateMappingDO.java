package top.aprdec.onepractice.entity;

import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;
import top.aprdec.onepractice.entity.proxy.PaperRateMappingDOProxy;

/**
 * 实体类。
 *
 * @author Aprdec
 * @since 1.0
 */
@Data
@Table(value = "paper_rate_mapping")
@EntityProxy
public class PaperRateMappingDO implements ProxyEntityAvailable<PaperRateMappingDO , PaperRateMappingDOProxy> {

    /**
     * paperid
     */
    @Column(primaryKey = true, value = "paperId")
    private Integer paperid;

    /**
     * 计算后总分
     */
    private Integer rating;

    /**
     * 投票人总数
     */
    private Integer number;


}
