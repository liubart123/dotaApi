package com.lojka.kurs.repository.app;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.model.user.EUserRoles;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.oracle.OracleDbRepository;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Slf4j
@Repository
public class UserRepository {
    static String hostName = "192.168.0.106";
    static String sid = "/pdb_kurs";
    static String userName = "pdb_kurs_app_admin";
    static String password = "password";
    Connection connection;

    String sqlGetUser = "begin get_user(?,?); end;";
    String sqlExistUser = "begin exist_user(?,?); end;";
    String sqlAddUser = "begin insert_user(?,?,?,?); end;";

    public Connection getConnection() throws SQLException, DbAccessException {
        if (connection==null || connection.isClosed()){
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521" + sid;

                connection = DriverManager.getConnection(connectionURL, userName,
                        password);
                return connection;
            } catch (ClassNotFoundException e) {
                throw new DbAccessException("drive class not found");
            } catch (SQLException e){
                throw new DbAccessException("error with connection to db");
            }
        }
        return connection;
    }

    public User getUser(String login) throws DbAccessException {
        log.debug("getting user " + login);
        try {
            CallableStatement cs = getConnection().prepareCall(sqlGetUser);
            cs.setString(2,login);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.executeQuery();
            ResultSet rs = cs.getObject(1, ResultSet.class);
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(login);
                user.setPassword(rs.getString("hash_password"));
                user.setRole(EUserRoles.valueOf(rs.getString("user_role")));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbAccessException("error with db query");
        }
        return null;
    }
    public Boolean existUser(String login) throws DbAccessException {
        log.debug("exist user " + login);
        try {
            CallableStatement cs = getConnection().prepareCall(sqlExistUser);
            cs.setString(1,login);
            cs.registerOutParameter(2, OracleTypes.PLSQL_BOOLEAN);
            cs.executeQuery();
            return cs.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbAccessException("error with db query");
        }
    }
    public void addUser(User user) throws DbAccessException {
        log.debug("add user " + user.getLogin());
        try {
            CallableStatement cs = getConnection().prepareCall(sqlAddUser);
            cs.setString(1,user.getLogin());
            cs.setString(2,user.getPassword());
            cs.setString(3,user.getRole().name());
            cs.registerOutParameter("resultId", OracleTypes.INTEGER);
            cs.executeQuery();
            if (cs.getInt("resultId")<0){
                throw new DbAccessException("error with db query");
            }
            user.setId(cs.getInt("resultId"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbAccessException("error with db query");
        }
    }

}
