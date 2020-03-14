package wang.kingweb.community.dto;

import lombok.Data;
import wang.kingweb.community.model.Comment;
import wang.kingweb.community.model.User;

@Data
public class CommentDTO extends Comment {
    private User user;
}
