package springboot.core.validator;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import springboot.entity.Result;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * 捕获controller层ValidationException参数验证报错
 */
@ControllerAdvice
public class ValidatorExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Result handler(ValidationException exception){
        if(exception instanceof ConstraintViolationException){
            ConstraintViolationException validatorException = (ConstraintViolationException)exception;
            Set<ConstraintViolation<?>> set = validatorException.getConstraintViolations();
            for(ConstraintViolation constrainViolation :set){
                return Result.fail(constrainViolation.getMessage());
            }
        }
        return Result.fail("服务器未处理的异常:"+exception.getMessage());

    }
}
