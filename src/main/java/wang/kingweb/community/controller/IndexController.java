package wang.kingweb.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wang.kingweb.community.entity.User;
import wang.kingweb.community.mapper.UserMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for (Cookie cookie:cookies){
                if("token".equals(cookie.getName())){
                    String token = cookie.getValue();
                    User user =  userMapper.selectUserByToken(token);
                    if(user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                }
            }
        }

        return "index";
    }
}
