package wang.kingweb.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.dto.PaginationDTO;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.mapper.ArticleMapper;
import wang.kingweb.community.model.ArticleExample;
import wang.kingweb.community.service.PaginationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    PaginationService paginationService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "size",defaultValue = "5") int size){

        int totalCount = (int)articleMapper.countByExample(new ArticleExample());
        PaginationDTO paginationDTO =  paginationService.getPageInfo(page,size,totalCount);

        model.addAttribute("paginationInfo",paginationDTO);


        //文章列表展示
        List<Article> articleList = articleMapper.selectArticleWithUser(null, paginationDTO.getOffset(), size);
        model.addAttribute("articleList",articleList);
        return "index";
    }
}
