package top.aprdec.onepractice.entity;

import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;
import top.aprdec.onepractice.entity.proxy.QuestionsDOProxy;
import top.aprdec.onepractice.entity.questionsubentity.String2ComplexType;
import top.aprdec.onepractice.entity.questionsubentity.matchingdata;
import top.aprdec.onepractice.entity.questionsubentity.option;
import top.aprdec.onepractice.entity.questionsubentity.wordbank;
import top.aprdec.onepractice.util.JsonConverter;

import java.lang.Object;
import java.util.List;

/**
 * 实体类。
 *
 * @author Aprdec
 * @since 1.0
 */
@Data
@Table(value = "questions")
@EntityProxy
public class QuestionsDO implements ProxyEntityAvailable<QuestionsDO , QuestionsDOProxy> {

    @Column(primaryKey = true, value = "question_id")
    private Integer questionId;

    private Integer paperId;

    /**
     * 写作/听力/阅读/翻译
     */
    private String partName;

    /**
     * A/B/C等部分
     */
    private String sectionName;

    private String questionType;

    private Integer questionOrder;

    /**
     * 题目内容或要求
     */
    private String content;

    /**
     * 统一答案存储
     */
    private String correctAnswer;

    /**
     * 选择题选项，格式: [{"label":"A","content":"选项内容"},...]
     */
    @Column(conversion = JsonConverter.class,complexPropType = option.class)
    private List<option> options;

    /**
     * 选词填空的单词库，格式: ["word1", "word2", ...]
     */
    @Column(conversion = JsonConverter.class, complexPropType = String2ComplexType.class)
    private List<String> wordBank;

    /**
     * 匹配题数据，格式: {"paragraphs": {"A":"段落内容",...}, "items": [{"id":1,"content":"匹配项内容"},...]}
     */
    @Column(conversion = JsonConverter.class)
    private matchingdata matchingData;


}
