package com.lojka.kurs.exception;

public class DbConnectionClosedException extends  Exception {
    public DbConnectionClosedException() {
        super();
    }
    public DbConnectionClosedException(String mes){
        super(mes);
    }
}
