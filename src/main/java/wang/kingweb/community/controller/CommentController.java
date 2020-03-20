package wang.kingweb.community.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import wang.kingweb.community.dto.CommentDTO;
import wang.kingweb.community.dto.RespDTO;
import wang.kingweb.community.enums.CommentType;
import wang.kingweb.community.enums.CustomizeErrorCode;
import wang.kingweb.community.model.Comment;
import wang.kingweb.community.model.User;
import wang.kingweb.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    @ResponseBody
    public RespDTO doComment(@RequestBody Comment comment,
                             HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return RespDTO.error(CustomizeErrorCode.NOT_LOGIN.getMessage());
        }
        comment.setCreateTime(System.currentTimeMillis());
        comment.setModifiedTime(System.currentTimeMillis());
        comment.setCommentor(user.getId());
        comment.setLikeCount(0);
        comment.setCommentCount(0);
        Integer result = commentService.insert(comment);
        if(result==1){
            return RespDTO.ok(CustomizeErrorCode.COMMENT_SUCCESS.getMessage(),user);
        }else {
            return RespDTO.error(CustomizeErrorCode.COMMENT_FAILED.getMessage());
        }
    }

    @GetMapping("/childComment")
    @ResponseBody
    public RespDTO getChildComments(@Param("childId") Long childId, HttpServletRequest request){
        if(StringUtils.isEmpty(childId)){
            return RespDTO.error(CustomizeErrorCode.COMMENT_NOT_FOUND.getMessage());
        }
        List<CommentDTO> commentDTOS = commentService.selectCommentsById(childId, CommentType.COMMENT.getType());
        return RespDTO.ok("获取二级评论成功",commentDTOS);
    }

}
