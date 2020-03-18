package wang.kingweb.community.enums;

public enum  NotificationInfoEnum {
    ARTICLE(1,"评论了文章"),
    COMMENT(2,"回复了评论")
    ;
    int type;
    String message;

    NotificationInfoEnum(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }


}
