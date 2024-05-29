package dao;

import vo.EventCalendarVo;
import vo.EventVo;

import java.sql.SQLException;

public interface EventDao {

    void createEvent(EventVo event) throws SQLException;

    // MANAGER CALENDAR
    void getAllEvent (EventCalendarVo eventCalendar) throws SQLException;

}
