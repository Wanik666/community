package wang.kingweb.community.model;

import lombok.Data;

@Data
public class Article {
    private Integer id;

    private String title;

    private String tag;

    private Long authorId;

    private Integer readCount;

    private Integer likeCount;

    private Integer answerCount;

    private Long createTime;

    private Long modifiedTime;

    private String description;

    private User user;


}