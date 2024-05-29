package dao;

import oracle.jdbc.OracleTypes;
import vo.EventCalendarVo;
import vo.EventVo;

import java.sql.*;

public class EventDaoImpl implements EventDao {

    private Connection connection;

    public EventDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void getAllEvent () throws SQLException {

        String sql = "{call user_pkg.get_calendar_events(?,?)}";

        try(CallableStatement callableStatement = connection.prepareCall(sql)) {
            EventCalendarVo getId = new EventCalendarVo();
            callableStatement.setInt(1, getId.getId());
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

            while (resultSet.next()) {
                String yyyymmdd = resultSet.getString("YYYYMMDD");
                String event = resultSet.getString("EVENT");
                String startDate = resultSet.getString("START_DATE");
                String endDate = resultSet.getString("END_DATE");

                System.out.println("Date: " + yyyymmdd + ", Event: " + event +
                        ", Start Date: " + startDate + ", End Date: " + endDate);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createEvent(EventVo event) {

    }

}


