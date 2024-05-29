package dao;

import oracle.jdbc.OracleTypes;
import vo.EventCalendarVo;
import vo.EventVo;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.Properties;

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
    public void createEvent(EventVo event) {};

    @Override
    public void getPersonalEvent(EventCalendarVo eventCal) throws SQLException {
        String sql = "{call user_pkg.get_calendar_events(?,?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
//            EventCalendarVo getId = new EventCalendarVo();
            callableStatement.setInt(1, eventCal.getId());
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);


            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(2)) {
                while (resultSet.next()) {
                    String yyyymmdd = resultSet.getString("YYYYMMDD");
                    String events = resultSet.getString("EVENT");
                    String startDate = resultSet.getString("START_DATE");
                    String endDate = resultSet.getString("END_DATE");

                    System.out.println("Date: " + yyyymmdd + ", Event: " + events +
                            ", Start Date: " + startDate + ", End Date: " + endDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Properties properties = new Properties();
        try (Reader reader = new FileReader("lib/oracle.properties")) {
            properties.load(reader);

            String url = properties.getProperty("url");
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                
                EventDaoImpl eventDao = new EventDaoImpl(connection);
                EventCalendarVo eventCal = new EventCalendarVo();
                
                //변수로 바꿀 것
                eventCal.setId(3);
                eventDao.getPersonalEvent(eventCal);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


