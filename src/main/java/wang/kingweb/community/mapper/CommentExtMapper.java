package wang.kingweb.community.mapper;

import wang.kingweb.community.model.Comment;

public interface CommentExtMapper {
    void incSubCommentCount(Comment comment);
}