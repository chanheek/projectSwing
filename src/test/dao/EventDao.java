package test.dao;

import test.vo.EventCalendarVo;
import test.vo.EventVo;

import java.sql.SQLException;

public interface EventDao {

    void createEvent(EventVo event);

    // MANAGER CALENDAR
    void getAllEvent () throws SQLException;

    //PERSONAL CALENDAR
    void getPersonalEvent (EventCalendarVo eventCalId) throws SQLException;

}
