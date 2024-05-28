package test.dao;

import db.DBConnection;
import test.vo.EventVo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class EventDaoImpl implements EventDao{
    @Override
    public void createEvent(EventVo event) {

    }

//    public void insertManagerEvent(String event, String startDate, String endDate, int eventCalendarId) {
//        String sql = "{call manager_pkg.insertManagerEvent(?, ?, ?, ?)}";
//
//        try (Connection conn = DBConnection.getConnection();
//             CallableStatement cstmt = conn.prepareCall(sql)) {
//
//            cstmt.setString(1, event);
//            cstmt.setString(2, startDate);
//            cstmt.setString(3, endDate);
//            cstmt.setInt(4, eventCalendarId);
//
//            cstmt.execute();
//            System.out.println("Event inserted successfully.");
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            throw new RuntimeException("Error while inserting event: " + ex.getMessage(), ex);
//        }
//    }
//    public static void main(String[] args) {
//        EventDaoImpl eventDao = new EventDaoImpl();
//
//        String event = "New Event";
//        String startDate = "2024-05-27";
//        String endDate = "2024-05-27";
//        int eventCalendarId = 1;
//
//        eventDao.insertManagerEvent(event, startDate, endDate, eventCalendarId);
//    }
}


