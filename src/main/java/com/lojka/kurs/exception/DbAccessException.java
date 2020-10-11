package com.lojka.kurs.exception;

public class DbAccessException extends Exception{

    public DbAccessException() {
        super();
    }
    public DbAccessException(String mes){
        super(mes);
    }
}
