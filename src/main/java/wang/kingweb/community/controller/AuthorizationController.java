package wang.kingweb.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.dto.AccessTokenParam;
import wang.kingweb.community.dto.GitHubUser;
import wang.kingweb.community.enums.CustomizeErrorCode;
import wang.kingweb.community.exception.CustomizeException;
import wang.kingweb.community.provider.GitHubProvider;
import wang.kingweb.community.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class AuthorizationController {
    @Autowired
    GitHubProvider gitHubProvider;

    @Autowired
    UserService userService;


    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.client.secret}")
    private String client_secret;
    @Value("${github.redirect.uri}")
    private String redirect_uri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){

        AccessTokenParam accessTokenParam = new AccessTokenParam();
        accessTokenParam.setCode(code);
        accessTokenParam.setState(state);
        accessTokenParam.setClient_id(client_id);
        accessTokenParam.setClient_secret(client_secret);
        accessTokenParam.setRedirect_uri(redirect_uri);
        String Token = gitHubProvider.getAccessToken(accessTokenParam);   //获取到access_token
        String accessToken = Token.split("&")[0].split("=")[1];
        //使用access_token获取用户信息
        GitHubUser gitHubUser = gitHubProvider.getGitHubUser(accessToken);
        if(gitHubUser!=null){
            String token = userService.createOrUpdateUser(gitHubUser);
            Cookie cookie = new Cookie("token", token);
            //设置cookie生命周期
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
        }else{
            log.error("github用户登录授权失败");
            throw new CustomizeException(CustomizeErrorCode.GITHUB_AUTH_WRONG);
        }
        return "redirect:/";
    }
}
