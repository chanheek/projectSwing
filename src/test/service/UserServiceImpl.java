package test.service;

import test.vo.UserVo;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class UserServiceImpl implements UserService{


    @Override
    public boolean LoginUser(UserVo user) throws SQLException {
        int id = user.getId();
        if (id == 1) {
            // 어디로 보낸다
            return true; // 관리자로 로그인 성공
        } else {
            // 학생으로 보낸다
            return false; // 학생으로 로그인 성공
        }
    }

    @Override
    public int CheckId (UserVo user) {
            int id = user.getId();
            return id;
    };





}
