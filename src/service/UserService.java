package service;


import vo.UserVo;

import java.sql.SQLException;

public interface UserService {
        boolean LoginUser( UserVo user) throws SQLException;
        int CheckId (UserVo user) ;
}
