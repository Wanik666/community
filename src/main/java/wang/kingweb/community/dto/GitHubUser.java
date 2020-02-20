package wang.kingweb.community.dto;

import lombok.Data;

@Data
public class GitHubUser {
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;

}
