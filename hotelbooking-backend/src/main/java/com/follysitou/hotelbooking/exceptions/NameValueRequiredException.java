package com.follysitou.hotelbooking.exceptions;

public class NameValueRequiredException extends  RuntimeException{
    public NameValueRequiredException(String message){
        super(message);
    }
}
