package wang.kingweb.community.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wang.kingweb.community.dto.RespDTO;
import wang.kingweb.community.mapper.TagMapper;
import wang.kingweb.community.model.Article;
import wang.kingweb.community.model.Tag;
import wang.kingweb.community.model.TagExample;
import wang.kingweb.community.model.User;
import wang.kingweb.community.mapper.ArticleMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PublishController {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    TagMapper tagMapper;

    @GetMapping("/publish")
    public String publish(@RequestParam(value = "search",required = false) String search, HttpServletRequest request,Model model){

        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        if(StringUtils.isBlank(search)){
            List<Tag> tags = tagMapper.selectByExample(new TagExample());

            Map<String, List<Tag>> tagCollect = tags.stream().collect(Collectors.groupingBy(Tag::getTagType));

            model.addAttribute("tags",tagCollect);
            model.addAttribute("article",new Article());
            return "publish";
        }
        return "redirect:/?search="+search;


    }

    @PostMapping("/publish")
    @ResponseBody
    public RespDTO doPublish(@RequestBody Article article,
                             HttpServletRequest request, Model model){

        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return RespDTO.error("暂未登录，请登录后重试！");
        }

        model.addAttribute("article",article);

        //检测数据是否为空
        if(StringUtils.isBlank(article.getTitle())){
            return RespDTO.error("请填写标题后提交！");
        }
        if(StringUtils.isBlank(article.getDescription())){
            return RespDTO.error("请填写内容后提交！");
        }
        if(StringUtils.isBlank(article.getTag())){
            return RespDTO.error("请填写标签后提交！");
        }
        //添加文章（问题）信息到数据库

        article.setModifiedTime(System.currentTimeMillis());
        int result;
        if(article.getId()!=null){
            //更新文章信息到数据库
            result = articleMapper.updateByPrimaryKeySelective(article);
        }else{
            article.setAuthorId(user.getId());
            article.setCreateTime(article.getModifiedTime());
            //添加文章（问题）信息到数据库
            result = articleMapper.insertSelective(article);
        }
        if(result!=1){
            //更新失败
            return RespDTO.error("服务器忙~~");
        }

        //添加成功后，重定向到首页
        return RespDTO.ok("发布成功！");
    }
}
