package top.aprdec.onepractice.entity;

import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;
import top.aprdec.onepractice.entity.proxy.UserExamRecordDOProxy;
import top.aprdec.onepractice.util.UUIDPrimaryKeyGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.lang.Object;

/**
 * 实体类。
 *
 * @author Aprdec
 * @since 1.0
 */
@Data
public class UserExamRecordDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8218159487816515908L;

    private String recordId;

    private Long userId;

    private Integer paperId;

    private String paperType;

    private String paperName;

    /**
     * 做题类型
     */
    private String type;

    /**
     * 是否结束?(对应是否中途退出) (1:是 0:否)
     */
    private Integer isfinished;

    /**
     * 用户提交的答案
     */
    private String answers;

    /**
     * 做卷时间
     */
    private Integer timespend;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 总分
     */
    private Integer totalscore;


}
