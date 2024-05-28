package test.service;

import test.vo.UserVo;
import java.sql.SQLException;

public interface UserService {


        boolean LoginUser( UserVo user) throws SQLException;
        int CheckId (UserVo user) ;
}
