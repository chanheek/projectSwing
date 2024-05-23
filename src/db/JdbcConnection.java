package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

//import oracle.jdbc.pool.OracleDataSource;

public class JdbcConnection {

    @SuppressWarnings("unused")
    public static void main(String args[]) throws SQLException {

//		Connection conn5 = DBConnection.getConnection();

        var conn5 = DBConnection.getConnection();

        System.out.println("성공");




//		OracleDataSource ods = new OracleDataSource();
//
//		/* Thin driver */
//
//		// 1
//		ods.setURL("jdbc:oracle:thin:@localhost:1521/xe");
//		ods.setUser("hr"); //아디
//		ods.setPassword("hr"); // 비번
//		Connection conn1 = ods.getConnection();
//
//		DatabaseMetaData meta = conn1.getMetaData();
//		System.out.println("JDBC driver version is " + meta.getDriverVersion()); //자르버전 확인
//
//		// 2
//		ods.setURL("jdbc:oracle:thin:hr/hr@localhost:1521/xe"); //아디/비번@
//		Connection conn2 = ods.getConnection();
//		System.out.println("2 성공");
//
//
//
//
//		/* Oracle Call Interface (OCI) driver */
//
//
//
//
//		// 1
//		ods.setURL("jdbc:oracle:oci8:@mydb");
//		ods.setUser("ace");
//		ods.setPassword("me");
//		Connection conn3 = ods.getConnection();
//		System.out.println("3 성공");
//
//		// 2
//		ods.setURL("jdbc:oracle:oci8:ace/me@mydb");
//		Connection conn4 = ods.getConnection();
//		System.out.println("4 성공");
//
//		/* 설정 파일 + 싱글턴 패턴 활용 접속 */
//		// Connection conn5 = DBConnection.getConnection();
    }

}
