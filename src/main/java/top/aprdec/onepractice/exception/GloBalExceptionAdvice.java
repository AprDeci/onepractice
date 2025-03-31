package top.aprdec.onepractice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.aprdec.onepractice.commmon.AResult;

@Slf4j
@RestControllerAdvice
public class GloBalExceptionAdvice {
    //全局异常处理
//    @ExceptionHandler(value = RuntimeException.class)
//    public AResult<Object> handleException(Exception e) {
//        return AResult.error(10001,e.getMessage());
//    }

}
