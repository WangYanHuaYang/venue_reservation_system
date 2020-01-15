package com.genolo.venue_reservation_system.exceptions;

import lombok.Data;

/**
 * @program: venue_reservation_system
 * @description: 自定义异常捕获
 * @author: WYHY
 * @create: 2020-01-14 09:27
 **/
@Data
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 4564124491192825748L;

    private int code;

    public CustomException() {
        super();
    }

    public CustomException(int code, String message) {
        super(message);
        this.setCode(code);
    }

}
