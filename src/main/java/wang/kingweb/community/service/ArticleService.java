package wang.kingweb.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.kingweb.community.mapper.ArticleMapper;
import wang.kingweb.community.model.Article;

@Service
public class ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    public Article selectArticleById(long id) {
        return articleMapper.selectArticleById(id);
    }

    public int deleteArticleById(Integer id) {
        return articleMapper.deleteByPrimaryKey(id);
    }
}
