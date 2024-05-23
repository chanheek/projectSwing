package db;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnection {

    @SuppressWarnings("unused")
    public static void main(String[] args) throws SQLException {
		Connection conn5 = DBConnection.getConnection();

        System.out.println("성공");

    }

}
