package test.dao;

import oracle.jdbc.OracleTypes;
import test.vo.UserVo;

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

            // Register the OUT parameter as a cursor
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

            callableStatement.execute();

            // Retrieve the cursor
            ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

            // Process the cursor
            while (resultSet.next()) {
                // Retrieve columns from the result set
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int grade = resultSet.getInt("GRADE");
                String major = resultSet.getString("MAJOR");

                user.setId(id);
                user.setName(name);
                user.setGrade(grade);
                user.setMajor(major);

                // Print or process the retrieved values
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Grade: " + grade);
                System.out.println("Major: " + major);
            }

            resultSet.close();
            System.out.println("유저 검색 완료");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createUser(UserVo user) throws SQLException {
        String sql = "{call Create_users.createUsers(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the input parameters
            callableStatement.setInt(1, user.getId());
            callableStatement.setString(2, user.getJobId());
            callableStatement.setString(3, user.getName());
            callableStatement.setInt(4, user.getGrade());
            callableStatement.setString(5, user.getMajor());
            callableStatement.setInt(6, user.getLoginId());
            callableStatement.setString(7, user.getPassword());
            callableStatement.setInt(8, user.getEventCalendarId());

            // Execute the stored procedure
            callableStatement.execute();
            System.out.println("유저 생성 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(UserVo user) throws SQLException {
        String sql = "{call Create_users.deleteUsers(?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setInt(1, user.getId());
            callableStatement.execute();
            System.out.println("유저 삭제 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(UserVo user) throws SQLException {
        String sql = "{ call Create_users.updateUsers(?,?,?,?,?,?) }";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setInt(1, user.getId());
            callableStatement.setString(2, user.getName());
            callableStatement.setInt(3, user.getGrade());
            callableStatement.setString(4, user.getMajor());
            callableStatement.setInt(5, user.getLoginId());
            callableStatement.setString(6, user.getPassword());

            callableStatement.execute();
            System.out.println("유저 업데이트 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
