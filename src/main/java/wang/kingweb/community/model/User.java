package wang.kingweb.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;

    private Long accountId;

    private String name;

    private String token;

    private Long createTime;

    private Long modifiedTime;

    private String avatarUrl;


}