package wang.kingweb.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String toLoginPage(){

        return "login";
    }

    @GetMapping("/logout")
    public String Logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("notification");
        Cookie cookie = new Cookie("token",null);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
