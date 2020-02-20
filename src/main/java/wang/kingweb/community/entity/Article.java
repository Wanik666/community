package wang.kingweb.community.entity;

import lombok.Data;

/**
 * 文章
 */
@Data
public class Article {
    private int id;
    private String title;
    private String description;
    private String tag;
    private Long authorId;
    private int readCount;    //阅读数
    private int answerCount;  //回复数
    private int likeCount;    //点赞数
    private Long createTime;
    private Long modifiedTime;
    private User user;
}
