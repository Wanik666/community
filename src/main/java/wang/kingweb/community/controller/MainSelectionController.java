package wang.kingweb.community.controller;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.dto.PaginationDTO;
import wang.kingweb.community.mapper.UserMapper;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.model.ArticleExample;
import wang.kingweb.community.model.User;
import wang.kingweb.community.mapper.ArticleMapper;
import wang.kingweb.community.service.PaginationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainSelectionController {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PaginationService paginationService;

    @GetMapping("/main/{selection}")
    public String article(@PathVariable(name = "selection") String selection,
                          @RequestParam(name = "page" ,defaultValue = "1") int page,
                          @RequestParam(name = "size" ,defaultValue = "5") int size,
                          HttpServletRequest request,
                          Model model){
        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andAuthorIdEqualTo(user.getId().longValue());
        long totalCount = articleMapper.countByExample(articleExample);

        PaginationDTO paginationDTO =  paginationService.getPageInfo(page,size,(int)totalCount);
        model.addAttribute("paginationInfo",paginationDTO);
        switch (selection){
            case "article":
                //查询当前用户的所有文章
                List<Article> myArticleList = articleMapper.selectArticleWithUser(user.getId(),paginationDTO.getOffset(), size);

                model.addAttribute("myArticleList",myArticleList);
                //用于判断当前的操作
                model.addAttribute("selection",selection);
                return "article";
            case "personal":
                return "personal";
            case "message":
                return "message";
            default:
                return "redirect:/";
        }
    }
}