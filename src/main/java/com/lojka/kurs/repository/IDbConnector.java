package com.lojka.kurs.repository;

import com.lojka.kurs.exception.DbAccessException;

public interface IDbConnector {
    IDbRepository getRepository() throws DbAccessException;
    IDbRepository getAdminRepository() throws DbAccessException;
}
