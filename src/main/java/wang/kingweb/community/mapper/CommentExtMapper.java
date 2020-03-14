package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import wang.kingweb.community.model.Comment;
import wang.kingweb.community.model.CommentExample;

import java.util.List;

public interface CommentExtMapper {
    void incSubCommentCount(Comment comment);
}