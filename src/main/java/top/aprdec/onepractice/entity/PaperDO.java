package top.aprdec.onepractice.entity;

import com.easy.query.core.annotation.*;
import com.easy.query.core.enums.RelationTypeEnum;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import top.aprdec.onepractice.entity.proxy.PaperDOProxy;
import top.aprdec.onepractice.util.easyentity.QuestionCountColumnValueSQLConverter;

import java.util.List;

/**
 * 实体类。
 *
 * @author Aprdec
 * @since 1.0
 */
@Data
@Table(value = "papers")
@EntityProxy
public class PaperDO implements ProxyEntityAvailable<PaperDO , PaperDOProxy> {

    @Column(primaryKey = true, value = "paper_id")
    private Integer paperId;

    private String paperName;

    private Integer examYear;

    private Integer examMonth;

    private Integer version;

    /**
     * 总时长(分钟)
     */
    private Integer totalTime;

    private String type;

    @Navigate(value= RelationTypeEnum.OneToMany,targetProperty = "paperId")
    private List<QuestionsDO> questions;

    @InsertIgnore
    @UpdateIgnore
    @Column(sqlConversion = QuestionCountColumnValueSQLConverter.class,autoSelect = false)
    private Long questionCount;


}
