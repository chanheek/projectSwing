package dao;

import vo.EventCalendarVo;
import vo.EventVo;

import java.sql.SQLException;
import java.time.LocalDate;

public interface EventDao {

    void createEvent(EventVo event) throws SQLException;
    void getAllEvent(EventCalendarVo eventCalendar) throws SQLException;
    void deleteEvent(int eventId) throws SQLException;
    int getEventId(EventVo eventVo, String event, LocalDate date) throws  SQLException;
}
