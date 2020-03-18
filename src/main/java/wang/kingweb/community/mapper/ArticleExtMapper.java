package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import wang.kingweb.community.dto.ArticleDTO;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.model.ArticleExample;

import java.util.List;


public interface ArticleExtMapper {
    List<ArticleDTO> selectArticleWithUser(@Param("uid") Long uid, @Param("offset") Long offset, @Param("size") Long size);

    ArticleDTO selectArticleById(@Param("id") long id);

    void incView(Article article);

    Integer incComment(Article article);

    List<Article> getAboutArticles(Article article);
}