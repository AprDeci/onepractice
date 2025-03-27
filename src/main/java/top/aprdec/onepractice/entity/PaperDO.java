package top.aprdec.onepractice.entity;

import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import top.aprdec.onepractice.entity.proxy.PaperDOProxy;

/**
 * 实体类。
 *
 * @author Aprdec
 * @since 1.0
 */
@Data
@Table(value = "papers")
@EntityProxy
public class PaperDO implements ProxyEntityAvailable<PaperDO, PaperDOProxy> {

    @Column(primaryKey = true, value = "paper_id")
    private Integer paperId;

    private String paperName;

    private String examYear;

    private Integer examMonth;

    private Integer version;

    /**
     * 总时长(分钟)
     */
    private Integer totalTime;

    private String paperType;


}
