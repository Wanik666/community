package wang.kingweb.community.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    //第三方登录id
    private Long accountId;
    //验证token
    private String token;
    private Long createTime;
    private Long modifiedTime;
    private String avatarUrl;

}
