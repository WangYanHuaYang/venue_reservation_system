package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.Msg;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: venue_reservation_system
 * @description: controller异常处理类
 * @author: WYHY
 * @create: 2020-01-09 13:46
 **/
@RestControllerAdvice
public class ValidParamExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Msg handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessage=new StringBuilder(bindingResult.getFieldErrors().size()*16);
//        errorMessage.append("Invalid Request  ");
        for (int i=0;i<bindingResult.getFieldErrors().size();++i){
            if (i>0){
                errorMessage.append(",");
            }
            FieldError fieldError=bindingResult.getFieldErrors().get(i);
//            errorMessage.append(fieldError.getField());
//            errorMessage.append(":");
            errorMessage.append(fieldError.getDefaultMessage());
        }
        return Msg.CUSTOM_MSG(0,errorMessage.toString());
    }
}
