package VO;

import java.util.Date;

public class EventVO {
    private int id;
    private String event;
    private Date startDate;
    private Date endDate;
    private int eventCalendarId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getEventCalendarId() {
        return eventCalendarId;
    }

    public void setEventCalendarId(int eventCalendarId) {
        this.eventCalendarId = eventCalendarId;
    }
}
