package wang.kingweb.community.dto;

import lombok.Data;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.model.User;

@Data
public class ArticleDTO extends Article {
    private User user;

}
