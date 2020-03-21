package wang.kingweb.community.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wang.kingweb.community.dto.ArticleDTO;
import wang.kingweb.community.enums.CommentType;
import wang.kingweb.community.mapper.ArticleExtMapper;
import wang.kingweb.community.mapper.ArticleMapper;
import wang.kingweb.community.mapper.CommentMapper;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.model.Comment;
import wang.kingweb.community.model.CommentExample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleExtMapper articleExtMapper;
    
    @Autowired
    CommentMapper commentMapper;

    public ArticleDTO selectArticleById(long id) {
        return articleExtMapper.selectArticleById(id);
    }

    @Transactional
    public int deleteArticleById(Long id) {
        //找到该文章的一级评论，然后根据一级评论去找二级评论，先删除二级评论，然后删除一级评论
        //查找一级评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(CommentType.ARTICLE.getType()).andParentIdEqualTo(id);
        List<Comment> firstComments = commentMapper.selectByExample(commentExample);
        //获取一级评论所有id
        List<Long> firstCommentId = firstComments.stream().map(Comment::getId).collect(Collectors.toList());
        if(firstCommentId!=null&&firstCommentId.size()>0){
            //根据一级评论id查找二级评论并删除
            CommentExample example = new CommentExample();
            example.createCriteria().andParentIdIn(firstCommentId).andTypeEqualTo(CommentType.COMMENT.getType());
            commentMapper.deleteByExample(example);

            //删除一级评论
            CommentExample firstComment = new CommentExample();
            firstComment.createCriteria().andIdIn(firstCommentId);
             commentMapper.deleteByExample(firstComment);
        }
        //删除文章

        return articleMapper.deleteByPrimaryKey(id);

    }

    public void incView(long id) {
        //进行数据库阅读数累加操作
        Article updateArticle = new Article();
        updateArticle.setId(id);
        updateArticle.setReadCount(1);
        articleExtMapper.incView(updateArticle);
    }

    public List<Article> getAboutArticles(Article article) {

        List<Article> aboutArticles = articleExtMapper.getAboutArticles(article);
        if(aboutArticles==null||aboutArticles.size()==0){
            return new ArrayList<>();
        }
        return aboutArticles;
    }
}
