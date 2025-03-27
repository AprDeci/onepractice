package top.aprdec.onepractice.eenum;

import lombok.Getter;

@Getter
public enum PaperTypeEnum {
    CET4("CET-4"),
    CET6("CET-6"),
    KY1("KY-1"),
    KY2("KY-2");

    private String type;

    PaperTypeEnum(String type) {
        this.type = type;
    }

    public String getvalue(){
        return this.type;
    }
}
