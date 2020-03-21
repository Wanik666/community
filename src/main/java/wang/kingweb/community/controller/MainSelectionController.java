package wang.kingweb.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.dto.ArticleDTO;
import wang.kingweb.community.dto.PaginationDTO;
import wang.kingweb.community.mapper.ArticleExtMapper;
import wang.kingweb.community.mapper.ArticleMapper;
import wang.kingweb.community.model.ArticleExample;
import wang.kingweb.community.model.User;
import wang.kingweb.community.service.PaginationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainSelectionController {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    ArticleExtMapper articleExtMapper;

    @Autowired
    private PaginationService paginationService;

    @GetMapping("/main/{selection}")
    public String article(@PathVariable(name = "selection") String selection,
                          @RequestParam(name = "search",required = false) String search,
                          @RequestParam(name = "page" ,defaultValue = "1") Long page,
                          @RequestParam(name = "size" ,defaultValue = "5") Long size,
                          HttpServletRequest request,
                          Model model){
        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andAuthorIdEqualTo(user.getId().longValue());
        //long totalCount = articleMapper.countByExample(articleExample);
        Long totalCount = articleExtMapper.countBySearch(user.getId(),search);
        PaginationDTO paginationDTO =  paginationService.getPageInfo(page,size,totalCount);
        model.addAttribute("paginationInfo",paginationDTO);
        if(StringUtils.isNotBlank(search)){
            model.addAttribute("search",search);
        }
        switch (selection){
            case "article":
                //查询当前用户的所有文章
                List<ArticleDTO> myArticleList = articleExtMapper.selectArticleWithUserBySearch(search,user.getId(),paginationDTO.getOffset(), size);

                model.addAttribute("myArticleList",myArticleList);
                //用于判断当前的操作
                model.addAttribute("selection",selection);
                return "article";
            case "personal":
                if(StringUtils.isBlank(search)){

                    return "personal";
                }else {
                    return "redirect:/?search="+search;
                }
            case "notification":
                if(StringUtils.isBlank(search)){
                    return "redirect:/notification";
                }else {
                    return "redirect:/?search="+search;
                }
            default:
                return "redirect:/";
        }
    }
}
