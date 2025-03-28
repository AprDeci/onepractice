package top.aprdec.onepractice.eenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionTypeEnum {
    LISTENING("listening"),
    READING("reading"),
    WRITING("writing"),
    CLOZE("cloze"),
    MATCHING("matching"),
    TRANSLATION("translation");

    private final String type;
}
