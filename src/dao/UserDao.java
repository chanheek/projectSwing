package dao;

import vo.EventCalendarVo;
import vo.UserVo;

import java.sql.SQLException;

public interface UserDao {
    void readUser(UserVo user) throws SQLException;
    void readUserCalendarInfo(EventCalendarVo eventCalendar) throws SQLException;
}
