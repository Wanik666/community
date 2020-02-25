package wang.kingweb.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.dto.PaginationDTO;
import wang.kingweb.community.entity.Article;
import wang.kingweb.community.entity.User;
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
        int totalCount = articleMapper.count(user.getId());
        PaginationDTO paginationDTO =  paginationService.getPageInfo(page,size,totalCount);
        model.addAttribute("paginationInfo",paginationDTO);
        switch (selection){
            case "article":
                //修改articleMapper中的list列表方法，我们这里传入用户id,如果id为空则表示查询所有，如果id不为空，则查询当前id的用户信息
                List<Article> myArticleList = articleMapper.list(user.getId(), paginationDTO.getOffset(), size);

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
