package wang.kingweb.community.enums;

public enum  NotificationStatusEnum {
    NOTIFICATION_READ(1),
    NOTIFICATION_UNREAD(0);
    ;
    int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
