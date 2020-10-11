package com.lojka.kurs.repository.oracle;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.repository.IDbConnection;
import com.lojka.kurs.repository.IDbRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDbRepository implements IDbRepository {
    static String hostName = "192.168.0.106";
    static String sid = "/pdb_kurs";
    static String userName = "pdb_kurs_admin2";
    static String password = "password";
    @Override
    public IDbConnection getConnection() throws DbAccessException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521" + sid;

            Connection conn = DriverManager.getConnection(connectionURL, userName,
                    password);
            IDbConnection res = new OracleDbConnection();
            res.setDbConnection(conn);
            return res;
        } catch (ClassNotFoundException e) {
            throw new DbAccessException("drive class not found");
        } catch (SQLException e){
            throw new DbAccessException("error with connection to db");
        }

    }

    @Override
    public IDbConnection getAdminConnection() throws DbAccessException {
        return null;
    }
}
