package wang.kingweb.community.enums;

public enum NotificationTypeEnum {
    NOTIFICATION_TYPE_OF_ARTICLE(1),
    NOTIFICATION_TYPE_OF_COMMENT(2)
    ;
    private int type;

    public int getType() {
        return type;
    }

    NotificationTypeEnum(int type) {
        this.type = type;
    }
}
