package com.lojka.kurs.repository;

import com.lojka.kurs.exception.DbAccessException;

public interface IDbRepository {
    IDbConnection getConnection() throws DbAccessException;
    IDbConnection getAdminConnection() throws DbAccessException;
}
