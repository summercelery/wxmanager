package springboot.core.spring;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import springboot.core.exception.BusinessException;
import springboot.wxcms.entity.Result;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * 捕获controller层ValidationException参数验证报错
 */
@ControllerAdvice
@Slf4j
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


    /**
     * 处理business异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Result processException(HttpServletRequest request, BusinessException e){
        log.error("业务异常：", e);
        String message = e.getMessage();
        return Result.fail(message);
    }


    /**
     * 处理异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Result processException(HttpServletRequest request, Exception e){
        log.error("处理异常：", e);
        String message = e.getMessage();
        return Result.fail(message);
    }

}
