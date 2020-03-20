package wang.kingweb.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.dto.ArticleDTO;
import wang.kingweb.community.dto.PaginationDTO;
import wang.kingweb.community.mapper.ArticleExtMapper;
import wang.kingweb.community.mapper.ArticleMapper;
import wang.kingweb.community.service.PaginationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleExtMapper articleExtMapper;

    @Autowired
    PaginationService paginationService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(value = "search",required = false) String search,
                        @RequestParam(value = "page",defaultValue = "1") Long page,
                        @RequestParam(value = "size",defaultValue = "5") Long size){

        //Long totalCount = articleMapper.countByExample(new ArticleExample());
        //如果有查询内容，根据查询内容获得总文章数
        Long totalCount = articleExtMapper.countBySearch(null,search);
        //如果有查询内容，根据查询内容获得分页数据
        PaginationDTO paginationDTO =  paginationService.getPageInfo(page,size,totalCount);

        model.addAttribute("paginationInfo",paginationDTO);

        //文章列表展示
        List<ArticleDTO> articleList = articleExtMapper.selectArticleWithUserBySearch(search,null, paginationDTO.getOffset(), size);
        model.addAttribute("articleList",articleList);
        if(StringUtils.isNotBlank(search)){
            model.addAttribute("search",search);
        }
        return "index";
    }
}
