package top.aprdec.onepractice.exception;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.eenum.ErrorEnum;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GloBalExceptionAdvice {

    @ExceptionHandler(value=Exception.class)
    public AResult<Object> handleException(Exception e) {
        log.error("系统异常：", e);
        return AResult.error(ErrorEnum.OPERATE_ERROR);
    }

    @ExceptionHandler(value=GeneralBusinessException.class)
    public AResult<Object> handleException(GeneralBusinessException e) {
        return AResult.error(e.getErrorEnum());
    }

    @ExceptionHandler(value=CommonException.class)
    public AResult<Object> handleException(CommonException e) {
        return AResult.error(500,e.getMessage());
    }


    @ExceptionHandler(value = NotLoginException.class)
public AResult<Object> handleException(NotLoginException e) {
    return AResult.error(ErrorEnum.TOKEN_INVALID);
    }

    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public  AResult<Object> handleException(MethodArgumentNotValidException ex) {
        try {
            List<ObjectError> errors = ex.getBindingResult().getAllErrors();
            String message = errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            log.error("param illegal: {}", message);
            return AResult.error(ErrorEnum.PARAM_IS_INVALID.getCode(), message);
        } catch (Exception e) {
            return AResult.error(ErrorEnum.SERVER_ERROR);
        }
    }

}
