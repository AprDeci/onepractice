package top.aprdec.onepractice.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

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

    private Long timestamp;

    private Long hasspendtime;


}
