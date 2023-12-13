package com.buaa.markpp.handler;

import com.buaa.markpp.model.ExceptionType;
import com.buaa.markpp.model.MarkppException;
import com.buaa.markpp.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.validation.ValidationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MarkppException.class)
    @ResponseBody
    public Result<Void> handleCustomException(MarkppException exception) {
        return new Result<Void>().withFailure(exception.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public Result<Void> handleValidationException(ValidationException exception) {
        return new Result<Void>().withFailure(ExceptionType.INVALID_PARAM.getMessage() + "：" + exception.getMessage());
    }

    @ExceptionHandler({
            HttpMessageConversionException.class,
            MethodArgumentNotValidException.class,
            TypeMismatchException.class,
            ServletException.class })
    @ResponseBody
    public Result<Void> handleParamException(Exception exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return new Result<Void>().withFailure(ExceptionType.INVALID_PARAM);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Void> handleOtherException(Exception exception) {
        exception.printStackTrace();
        return new Result<Void>().withFailure(ExceptionType.INTERNAL_ERROR);
    }
}
