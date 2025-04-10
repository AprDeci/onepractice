package top.aprdec.onepractice.exception;

import lombok.Getter;
import lombok.Setter;
import top.aprdec.onepractice.eenum.ErrorEnum;

import java.io.Serial;

@Getter
public class GeneralBusinessException  extends RuntimeException{
    private ErrorEnum errorEnum;

    @Serial
    private static final long serialVersionUID = 6951352528531405814L;

    public GeneralBusinessException(ErrorEnum errorEnum) {
        this.errorEnum = errorEnum;
    }
}
