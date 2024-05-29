package dao;

import vo.EventCalendarVo;
import vo.EventVo;

import java.sql.SQLException;

public interface EventDao {

    void createEvent(EventVo event) throws SQLException;
    void getAllEvent(EventCalendarVo eventCalendar) throws SQLException;
    void deleteEvent(EventVo event) throws SQLException;

}
