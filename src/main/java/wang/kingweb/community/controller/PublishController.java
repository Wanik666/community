package wang.kingweb.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.entity.Article;
import wang.kingweb.community.entity.User;
import wang.kingweb.community.mapper.ArticleMapper;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    ArticleMapper articleMapper;

    @GetMapping("/publish")
    public String publish(HttpServletRequest request){

        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        return "publish";

    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(name = "title") String title,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "tag") String tag,
                            HttpServletRequest request, Model model){

        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            model.addAttribute("error","暂未登录，请登录后重试！");
            return "publish";
        }

        //用于数据回显
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        //检测数据是否为空
        if(title==null||title==""){
            model.addAttribute("error","请填写标题后提交！");
            return "publish";
        }
        if(description==null||description==""){
            model.addAttribute("error","请填写内容后提交！");
            return "publish";
        }
        if(tag==null||tag==""){
            model.addAttribute("error","请填写标签后提交！");
            return "publish";
        }

        //添加文章（问题）信息到数据库
        Article article = new Article();
        article.setAuthorId(user.getId());
        article.setTitle(title);
        article.setDescription(description);
        article.setTag(tag);
        article.setCreateTime(System.currentTimeMillis());
        article.setModifiedTime(article.getCreateTime());

        int result = articleMapper.addArticle(article);
        if(result!=1){
            model.addAttribute("error","发布失败，请稍后提交！");
            return "publish";
        }
        //添加成功后，重定向到首页
        return "redirect:/";
    }
}
