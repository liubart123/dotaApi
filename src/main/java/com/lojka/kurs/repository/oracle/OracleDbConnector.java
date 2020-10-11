package com.lojka.kurs.repository.oracle;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.IDbConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDbConnector implements IDbConnector {
    static String hostName = "192.168.0.106";
    static String sid = "/pdb_kurs";
    static String userName = "pdb_kurs_admin2";
    static String password = "password";
    @Override
    public IDbRepository getRepository() throws DbAccessException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521" + sid;

            Connection conn = DriverManager.getConnection(connectionURL, userName,
                    password);
            IDbRepository res = new OracleDbRepository();
            res.setDbConnection(conn);
            return res;
        } catch (ClassNotFoundException e) {
            throw new DbAccessException("drive class not found");
        } catch (SQLException e){
            throw new DbAccessException("error with connection to db");
        }

    }

    @Override
    public IDbRepository getAdminRepository() throws DbAccessException {
        return null;
    }
}
