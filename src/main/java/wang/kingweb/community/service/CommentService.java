package wang.kingweb.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import wang.kingweb.community.dto.CommentDTO;
import wang.kingweb.community.enums.CommentType;
import wang.kingweb.community.enums.CustomizeErrorCode;
import wang.kingweb.community.exception.CustomizeException;
import wang.kingweb.community.mapper.*;
import wang.kingweb.community.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleExtMapper articleExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment){
        Long parentId = null;
        Article article1 = articleMapper.selectByPrimaryKey(comment.getParentId());
        if(comment.getType()==null || !CommentType.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_WRONG);
        }

        //对文章进行评论
        if(comment.getType()==CommentType.ARTICLE.getType()){
            if(comment.getParentId()==null || articleMapper.selectByPrimaryKey(comment.getParentId())==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_ARTICLE_ID_NOT_FOUND);
            }
            parentId = comment.getParentId();
        }else {
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(parentComment!=null){
                parentId = parentComment.getParentId();
            }else {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_ARTICLE_ID_NOT_FOUND);
            }
            Comment updateComment = new Comment();
            updateComment.setCommentCount(1);
            updateComment.setId(comment.getParentId());
            commentExtMapper.incSubCommentCount(updateComment);
            if(comment.getParentId()==null || commentMapper.selectByPrimaryKey(comment.getParentId())==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
        }
        //添加评论内容
        commentMapper.insert(comment);
        Article article = new Article();
        //评论数递增
        article.setId(parentId);
        article.setAnswerCount(1);
        articleExtMapper.incComment(article);
    }

    public List<CommentDTO> selectCommentsById(long id,int type) {

        List<CommentDTO> commentDTOS  = new ArrayList<>();
        /** 查询出当前文章的所有一级评论 **/
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type);
        if(type==CommentType.ARTICLE.getType()){

            commentExample.setOrderByClause("create_time desc");
        }else if(type == CommentType.COMMENT.getType()){
            commentExample.setOrderByClause("create_time asc");
        }
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()<=0){
            return new ArrayList<>();
        }
        /** 使用lambda表达式获取用户id  **/
        Set<Long> ids = comments.stream().map(c -> c.getCommentor()).collect(Collectors.toSet());
        UserExample user = new UserExample();
        user.createCriteria().andIdIn(new ArrayList<>(ids));
        List<User> users = userMapper.selectByExample(user);
        //将获取到的用户List转为map
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u, (k1, k2) -> k1));
        //遍历并拷贝赋值
        comments.stream().forEach(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentor()));
            commentDTOS.add(commentDTO);
        });

        return commentDTOS;
    }
}
