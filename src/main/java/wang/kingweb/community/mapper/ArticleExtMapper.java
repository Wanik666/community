package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.Param;
import wang.kingweb.community.dto.ArticleDTO;
import wang.kingweb.community.model.Article;

import java.util.List;


public interface ArticleExtMapper {
    List<ArticleDTO> selectArticleWithUser( @Param("uid") Long uid, @Param("offset") Long offset, @Param("size") Long size);
    List<ArticleDTO> selectArticleWithUserBySearch(@Param("search") String search, @Param("uid") Long uid, @Param("offset") Long offset, @Param("size") Long size);

    ArticleDTO selectArticleById(@Param("id") long id);

    void incView(Article article);

    Integer incComment(Article article);

    List<Article> getAboutArticles(Article article);

    Long countBySearch(@Param("id") Long id,@Param("search") String search);
}