package wang.kingweb.community.enums;

public enum CustomizeErrorCode{
    SYS_ERROR("服务器出错了，过一会再试试吧"),
    GITHUB_AUTH_WRONG("GitHub服务器当前不稳定，过一会再登录试试~~"),
    FILE_NOT_FOUND("您要找的资源不存在或者被删除了，您可以查看其他文章"),
    COMMENT_ARTICLE_ID_NOT_FOUND("需要评论的文章不存在或被删除了，请查看其他文章"),
    NOT_LOGIN("您当前尚未登录，登录后方可进行评论!"),
    COMMENT_TYPE_WRONG("当前发表评论类型异常，请重新评论"),
    COMMENT_NOT_FOUND("当前评论不存在或被删除了，请刷新页面查看其他评论!"),
    COMMENT_SUCCESS("评论成功！"),
    COMMENT_FAILED("评论失败！"),

    ;
    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
