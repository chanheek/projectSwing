package test.vo;

public class EventCalendarVo {
    private int id;
    private String name;
    private String use;
    private int usersId;
    private int usersLoginId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    public int getUsersLoginId() {
        return usersLoginId;
    }

    public void setUsersLoginId(int usersLoginId) {
        this.usersLoginId = usersLoginId;
    }
}
