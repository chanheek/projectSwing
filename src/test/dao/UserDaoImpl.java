package test.dao;

import test.vo.UserVo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    private Connection connection;


    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void createUser(UserVo user)
            throws SQLException {
        String sql = "{call Create_users.createUsers(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the input parameters
            callableStatement.setInt(1, user.getId());  // p_id
            callableStatement.setString(2, user.getJobId()); // p_job_id
            callableStatement.setString(3, user.getName()); // p_name
            callableStatement.setInt(4, user.getGrade()); // p_grade
            callableStatement.setString(5, user.getMajor()); // p_major
            callableStatement.setInt(6, user.getLoginId()); // p_login_id
            callableStatement.setString(7, user.getPassword()); // p_password
            callableStatement.setInt(8, user.getEventCalendarId()); // p_event_calendar

            // Execute the stored procedure
            callableStatement.execute();
            System.out.println("유저 생성 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public  void delectUser(UserVo user) throws SQLException {

        String sql = "{call Create_users.deleteUsers(?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)){
            callableStatement.setInt(1, user.getId());
            System.out.println("유저 삭제 성공");
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void updateUser(UserVo user) throws SQLException {

        String sql = "{ call Create_users.updateUsers(?,?,?,?,?,?) }";

        try (CallableStatement callableStatement = connection.prepareCall(sql)){

            callableStatement.setInt(1, user.getId());
            callableStatement.setString(2, user.getName());
            callableStatement.setInt(3, user.getGrade());
            callableStatement.setString(4, user.getMajor());
            callableStatement.setInt(5, user.getLoginId());
            callableStatement.setString(6, user.getPassword());


            System.out.println("유저 업데이트 성공");
            callableStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
