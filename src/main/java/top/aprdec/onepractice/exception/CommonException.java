package top.aprdec.onepractice.exception;

import java.io.Serial;

public class CommonException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -1630364322642898400L;

    public CommonException(String s) {
        super  (s);
    }
}
