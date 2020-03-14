package wang.kingweb.community.enums;

public enum CommentType {
    ARTICLE(1),
    COMMENT(2),
    ;
    int type;

    CommentType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isExist(Integer type) {
        for (CommentType commentTypeEnum : CommentType.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
