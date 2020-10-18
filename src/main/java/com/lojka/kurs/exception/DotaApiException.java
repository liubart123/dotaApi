package com.lojka.kurs.exception;

public class DotaApiException extends Exception{
    public DotaApiException() {
        super();
    }
    public DotaApiException(String mes){
        super(mes);
    }
}
