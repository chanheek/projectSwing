package dao;

import vo.UserVo;

import java.sql.SQLException;

public interface UserDao {
    void createUser( UserVo user  ) throws SQLException;
    void deleteUser(UserVo user) throws SQLException;
    void updateUser(UserVo user) throws SQLException;
    void readUser(UserVo user) throws SQLException;

}
