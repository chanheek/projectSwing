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
    public void getAllEvent(EventCalendarVo eventCalendar) throws SQLException {
        String sql = "{call user_pkg.get_calendar_events(?,?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // 입력 파라미터 설정
            callableStatement.setInt(1, eventCalendar.getId());
            System.out.println("Setting calendar ID: " + eventCalendar.getId());
            // 출력 파라미터 등록
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
            // 프로시저 실행
            System.out.println("Executing stored procedure...");
            callableStatement.execute();

            // 결과 커서 받기
            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(2)) {
                // 결과가 있는지 확인
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No events found.");
                } else {
                    // 결과 출력
                    while (resultSet.next()) {
                        String yyyymmdd = resultSet.getString("yyyymmdd");
                        String event = resultSet.getString("event");
                        String startDate = resultSet.getString("start_date");
                        String endDate = resultSet.getString("end_date");

                        System.out.println("Date: " + yyyymmdd + ", Event: " + event +
                                ", Start Date: " + startDate + ", End Date: " + endDate);
                    }
                }
            }
        } catch (SQLException e) {
            // 예외 처리
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void createEvent(EventVo event) {
        // 구현 필요
    }
}
