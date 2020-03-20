package wang.kingweb.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wang.kingweb.community.dto.CommentDTO;
import wang.kingweb.community.enums.CommentType;
import wang.kingweb.community.enums.CustomizeErrorCode;
import wang.kingweb.community.enums.NotificationStatusEnum;
import wang.kingweb.community.enums.NotificationTypeEnum;
import wang.kingweb.community.exception.CustomizeException;
import wang.kingweb.community.mapper.*;
import wang.kingweb.community.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    NotificationMapper notificationMapper;

    @Transactional
    public Integer insert(Comment comment){
        //评论的对象，可能是文章id，也可能是评论id
        Long parentId = null;

        if(comment.getType()==null || !CommentType.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_WRONG);
        }

        //对文章进行评论
        if(comment.getType()==CommentType.ARTICLE.getType()){
            //文章的id
            parentId = comment.getParentId();
            Article selectArticle = null;
            if(parentId!=null){
                selectArticle = articleMapper.selectByPrimaryKey(parentId);

            }
            if( selectArticle != null){
                //添加通知信息到notification表
                insertNotificationInfo(comment, selectArticle.getAuthorId(),selectArticle,NotificationTypeEnum.NOTIFICATION_TYPE_OF_ARTICLE.getType());

            }else{
                throw new CustomizeException(CustomizeErrorCode.COMMENT_ARTICLE_ID_NOT_FOUND);
            }
        }else {
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            Article article = null;
            if(parentComment!=null){
                parentId = parentComment.getParentId();
                if(parentId!=null){
                    article = articleMapper.selectByPrimaryKey(parentId);
                }
            }
            if( article != null){
                //回复评论者
                insertNotificationInfo(comment, parentComment.getCommentor(),article,NotificationTypeEnum.NOTIFICATION_TYPE_OF_COMMENT.getType());

                insertNotificationInfo(comment, article.getAuthorId(),article,NotificationTypeEnum.NOTIFICATION_TYPE_OF_ARTICLE.getType());

                //回复文章作者
            }else {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_ARTICLE_ID_NOT_FOUND);
            }
            //查询评论的文章
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
        return articleExtMapper.incComment(article);
    }

    public Integer insertNotificationInfo(Comment comment, Long id,Article article, int type) {
        if(id == comment.getCommentor()) return null;
        Notification notification = new Notification();
        notification.setOuterId(article.getId());
        notification.setSenderId(comment.getCommentor());
        notification.setStatus(NotificationStatusEnum.NOTIFICATION_UNREAD.getStatus());
        notification.setCreateTime(System.currentTimeMillis());
        notification.setType(type);
        notification.setReceiverId(id);
        notification.setArticleTitle(article.getTitle());
        return notificationMapper.insert(notification);
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
