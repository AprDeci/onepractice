package top.aprdec.onepractice.exception;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.eenum.ErrorEnum;

@Slf4j
@RestControllerAdvice
public class GloBalExceptionAdvice {

    @ExceptionHandler(value=GeneralBusinessException.class)
    public AResult<Object> handleException(GeneralBusinessException e) {
        return AResult.error(e.getErrorEnum());
    }


    @ExceptionHandler(value = NotLoginException.class)
public AResult<Object> handleException(NotLoginException e) {
    return AResult.error(ErrorEnum.TOKEN_INVALID);
    }

}
