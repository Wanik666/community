package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import wang.kingweb.community.entity.Article;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert("insert into article (title,description,tag,author_id,create_time,modified_time) values " +
            "(#{title},#{description},#{tag},#{authorId},#{createTime},#{modifiedTime})")
    int addArticle(Article article);

    //查询文章列表信息
    @Select({"<script> " +
            "select u.*,a.id as aid,a.title,a.author_id, a.description,a.read_count,a.answer_count,a.like_count,a.create_time from article a,user u " +
            "where a.author_id = u.id " +
            "<if test='uId!=null'> and a.author_id=#{uId}</if>"+
            "limit #{page},#{size}"+
            "</script>"})
    @Results(id="ArticleUser",value = {
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "id",column = "aid"),
            @Result(property = "title",column = "title"),
            @Result(property = "description",column = "description"),
            @Result(property = "authorId",column = "author_id"),
            @Result(property = "readCount",column = "read_count"),
            @Result(property = "answerCount",column = "answer_count"),
            @Result(property = "likeCount",column = "like_count"),
            @Result(property = "user.name",column = "name"),
            @Result(property = "user.avatarUrl",column = "avatar_url")
    })
    List<Article> list(@Param(value = "uId") Long uId, @Param(value = "page") int page,@Param(value = "size") int size);

    //查询总文章数
    @Select({"<script> " +
            "select count(1) from article a,user u where a.author_id = u.id"+
            "<if test='uId!=null'> and u.id=#{uId}</if>"+
            "</script>"})
    int count(@Param(value = "uId") Long uId);


}
