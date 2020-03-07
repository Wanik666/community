package wang.kingweb.community.enums;

public enum CustomizeErrorCode{
    SYS_ERROR("服务器出错了，过一会再试试吧"),
    FILE_NOT_FIND("您要找的资源不存在或者被删除了，您可以查看其他文章"),
    ;
    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
