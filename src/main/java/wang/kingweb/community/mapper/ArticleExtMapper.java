package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.model.ArticleExample;

import java.util.List;


public interface ArticleExtMapper {
    List<Article> selectArticleWithUser(@Param("uid") Long uid, @Param("offset") int offset, @Param("size") int size);

    Article selectArticleById(@Param("id") long id);

    void incView(Article article);

    void incComment(Article article);
}