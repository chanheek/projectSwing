package dao;

import oracle.jdbc.OracleTypes;
import vo.CalendarVo;
import vo.EventCalendarVo;
import vo.UserVo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    private Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void readUser(UserVo user) throws SQLException {
        String sql = "{call user_pkg.getStudentId(?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setInt(1, user.getLoginId());
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int grade = resultSet.getInt("GRADE");
                String major = resultSet.getString("MAJOR");

                user.setId(id);
                user.setName(name);
                user.setGrade(grade);
                user.setMajor(major);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readUserCalendarInfo(EventCalendarVo eventCalendar) throws SQLException {
        String sql = "{call user_pkg.getUserCalendarInfo(?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, eventCalendar.getUsersLoginId());

            // Register the OUT parameter as a cursor
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

            callableStatement.execute();

            // Retrieve the cursor
            ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

            // Process the cursor
            while (resultSet.next()) {
                // Retrieve columns from the result set
                int event_calendar_id = resultSet.getInt("ID");
                eventCalendar.setId(event_calendar_id);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
