package wang.kingweb.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wang.kingweb.community.dto.ArticleDTO;
import wang.kingweb.community.dto.CommentDTO;
import wang.kingweb.community.dto.RespDTO;
import wang.kingweb.community.enums.CommentType;
import wang.kingweb.community.enums.CustomizeErrorCode;
import wang.kingweb.community.exception.CustomizeException;
import wang.kingweb.community.mapper.TagMapper;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.model.Tag;
import wang.kingweb.community.model.TagExample;
import wang.kingweb.community.model.User;
import wang.kingweb.community.service.ArticleService;
import wang.kingweb.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;
    @Autowired
    private TagMapper tagMapper;


    @GetMapping("/detail/{id}")
    public String articleDetails(@RequestParam(value = "search",required = false) String search,
                                 @PathVariable(name = "id") long id,
                                 Model model,HttpServletRequest request){
        if(StringUtils.isNotBlank(search)){
            return "redirect:/?search="+search;
        }

        ArticleDTO article = articleService.selectArticleById(id);
        //没有找到对应的文章信息，将异常抛出
        if(article==null){
            log.error(String.format("没有找到id: %d 的文章",id));
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

        List<Article> aboutArticles = articleService.getAboutArticles(article);



        model.addAttribute("aboutArticles",aboutArticles);
        model.addAttribute("comments",commentDTOList);
        model.addAttribute("ArticleDetail",article);
        return "articleDetail";
    }

    @GetMapping("/edit")
    public String editArticle(@RequestParam(value = "search",required = false) String search,
                              @RequestParam("id") Long id, Model model, HttpServletRequest request){
        if(StringUtils.isNotBlank(search)){
            return "redirect:/?search="+search;
        }
        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            model.addAttribute("error","暂未登录，请登录后重试！");
            return "redirect:/";
        }

        ArticleDTO article = articleService.selectArticleById(id);
        //没有找到对应的文章信息，将异常抛出
        if(article==null){
            log.error(String.format("没有找到id: %d 的文章",id));
            throw new CustomizeException(CustomizeErrorCode.FILE_NOT_FOUND);
        }

        List<Tag> tags = tagMapper.selectByExample(new TagExample());

        Map<String, List<Tag>> tagCollect = tags.stream().collect(Collectors.groupingBy(Tag::getTagType));

        model.addAttribute("tags",tagCollect);
        model.addAttribute("article",article);
        return "publish";
    }
    @DeleteMapping("/delete")
    @ResponseBody
    public RespDTO delArticle(@RequestParam("id") Long id){
        int result = articleService.deleteArticleById(id);
        if(result==1){

            return RespDTO.ok("删除成功！");
        }else{
            return RespDTO.error("删除失败！");
        }
    }
}
