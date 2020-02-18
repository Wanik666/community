package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import wang.kingweb.community.entity.Article;

@Mapper
public interface ArticleMapper {

    @Insert("insert into article (title,description,tag,author_id,create_time,modified_time) values " +
            "(#{title},#{description},#{tag},#{authorId},#{createTime},#{modifiedTime})")
    int addArticle(Article article);
}
