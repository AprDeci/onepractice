package top.aprdec.onepractice.entity;

import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;
import top.aprdec.onepractice.entity.proxy.VoteDOProxy;

/**
 * 实体类。
 *
 * @author Aprdec
 * @since 1.0
 */
@Data
@Table(value = "vote")
@EntityProxy
public class VoteDO extends BaseDO implements ProxyEntityAvailable<VoteDO , VoteDOProxy> {

    @Column(primaryKey = true, value = "Vote_id")
    private Integer voteId;

    private Long userId;

    private Integer paperId;

    /**
     * 1-5
     */
    private Integer rating;


}
