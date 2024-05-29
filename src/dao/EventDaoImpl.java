package dao;

import oracle.jdbc.internal.OracleTypes;
import vo.EventCalendarVo;
import vo.EventVo;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EventDaoImpl implements EventDao {

    private Connection connection;

    public EventDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void getAllEvent(EventCalendarVo eventCalendar) throws SQLException {
        String sql = "{call user_pkg.get_calendar_events(?,?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setInt(1, eventCalendar.getId());
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(2);
            Map<LocalDate, String[]> events = new HashMap<>();

            while (resultSet.next()) {
                LocalDate date = LocalDate.parse(resultSet.getString("YYYYMMDD"));
                String event = resultSet.getString("EVENT");

                events.putIfAbsent(date, new String[3]);
                String[] eventList = events.get(date);

                for (int i = 0; i < eventList.length; i++) {
                    if (eventList[i] == null) {
                        eventList[i] = event;
                        break;
                    }
                }
            }
            resultSet.close();
            eventCalendar.setEvents(events);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createEvent(EventVo event) throws SQLException {
        String sql = "{call user_pkg.insertUserEvent(?,?,?,?)}"; // Assuming you have a stored procedure to handle event creation

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setString(1, event.getEvent());
            callableStatement.setDate(2, Date.valueOf(event.getStartDate()));
            callableStatement.setDate(3, Date.valueOf(event.getEndDate()));
            callableStatement.setInt(4, event.getEventCalendarId());

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEvent(EventVo event) throws SQLException {
        String sql = "{call user_pkg.deleteUserEvent(?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setInt(1, event.getId());

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
