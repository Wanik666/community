package wang.kingweb.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wang.kingweb.community.dto.CommentDTO;
import wang.kingweb.community.enums.CommentType;
import wang.kingweb.community.enums.CustomizeErrorCode;
import wang.kingweb.community.exception.CustomizeException;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.model.User;
import wang.kingweb.community.service.ArticleService;
import wang.kingweb.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/article/detail/{id}")
    public String articleDetails(@PathVariable(name = "id") long id,
                                 Model model,HttpServletRequest request){
        Article article = articleService.selectArticleById(id);
        //没有找到对应的文章信息，将异常抛出
        if(article==null){
            throw new CustomizeException(CustomizeErrorCode.FILE_NOT_FOUND);
        }
        //进行阅读数增加,防止页面刷新增加阅读数
        HttpSession session = request.getSession();
        synchronized (session){
            if(session.getAttribute("article"+id)==null){
                session.setAttribute("article"+id,id);
                articleService.incView(id);
                article.setReadCount(article.getReadCount()+1);
            }
        }

        List<CommentDTO> commentDTOList =  commentService.selectCommentsById(id, CommentType.ARTICLE.getType());

        model.addAttribute("comments",commentDTOList);
        model.addAttribute("ArticleDetail",article);
        return "articleDetail";
    }

    @GetMapping("/article/{operate}")
    public String editOrDelArticle(@PathVariable(name = "operate") String operate,
                                   Long id, Model model, HttpServletRequest request){
        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            model.addAttribute("error","暂未登录，请登录后重试！");
            return "redirect:/";
        }
        switch (operate){
            case "edit":
                Article article = articleService.selectArticleById(id);
                //没有找到对应的文章信息，将异常抛出
                if(article==null){
                    throw new CustomizeException(CustomizeErrorCode.FILE_NOT_FOUND);
                }
                model.addAttribute("article",article);
                return "publish";
            case "delete":
                int result =  articleService.deleteArticleById(id);
                if (result!=1){
                    //操作失败
                }
                return "redirect:/main/article";
        }
        return "redirect:/";
    }
}
