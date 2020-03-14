package wang.kingweb.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.kingweb.community.mapper.ArticleExtMapper;
import wang.kingweb.community.mapper.ArticleMapper;
import wang.kingweb.community.model.Article;

@Service
public class ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleExtMapper articleExtMapper;

    public Article selectArticleById(long id) {
        return articleExtMapper.selectArticleById(id);
    }

    public int deleteArticleById(Long id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

    public void incView(long id) {
        //进行数据库阅读数累加操作
        Article updateArticle = new Article();
        updateArticle.setId(id);
        updateArticle.setReadCount(1);
        articleExtMapper.incView(updateArticle);
    }
}
