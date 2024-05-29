package vo;

public class EventVo {
    private int id;
    private String event;
    private String startDate;
    private String endDate;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getEventCalendarId() {
        return eventCalendarId;
    }

    public void setEventCalendarId(int eventCalendarId) {
        this.eventCalendarId = eventCalendarId;
    }
}
