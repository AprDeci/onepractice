package top.aprdec.onepractice.exception;

import cn.dev33.satoken.exception.NotLoginException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
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
        if(e.getData() != null) {
            return AResult.error(e.getErrorEnum(), e.getData());
        }else {
            return AResult.error(e.getErrorEnum());
        }
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

    @ExceptionHandler(ConstraintViolationException.class)
    public AResult<Object> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .distinct()
                .collect(Collectors.joining(","));
        return AResult.error(ErrorEnum.PARAM_IS_INVALID.getCode(), message);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public AResult<Object> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        return AResult.error(ErrorEnum.PARAM_IS_INVALID.getCode(), "参数无效");
    }

}
