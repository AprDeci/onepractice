package top.aprdec.onepractice.exception;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.eenum.CodeEnum;

@Slf4j
@RestControllerAdvice
public class GloBalExceptionAdvice {
    //全局异常处理
//    @ExceptionHandler(value = RuntimeException.class)
//    public AResult<Object> handleException(Exception e) {
//        return AResult.error(10001,e.getMessage());
//    }

    @ExceptionHandler(value = NotLoginException.class)
public AResult<Object> handleException(NotLoginException e) {
    return AResult.error(CodeEnum.NOTLOGIN_ERROR.getCode(), e.getMessage());
    }

}
