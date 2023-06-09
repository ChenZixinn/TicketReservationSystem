package com.ticket.reservation.exception;

/**
 *  异常类
 */
public class TicketSystemException extends RuntimeException{
    private final Integer code;
    private final String message;

    public TicketSystemException(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public TicketSystemException(TicketSystemExceptionEnum ticketSystemExceptionEnum){
        this(ticketSystemExceptionEnum.getCode(), ticketSystemExceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
