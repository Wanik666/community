package wang.kingweb.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.entity.AccessTokenParam;
import wang.kingweb.community.entity.GitHubUser;
import wang.kingweb.community.provider.GitHubProvider;

@Controller
public class AuthorizationController {
    @Autowired
    GitHubProvider gitHubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){

        AccessTokenParam accessTokenParam = new AccessTokenParam();
        accessTokenParam.setCode(code);
        accessTokenParam.setState(state);
        accessTokenParam.setClient_id("b5bf62ea34e5a9ba7cf5");
        accessTokenParam.setClient_secret("0b08019a841e39355d6fe070209e7f60ac2bce18");
        accessTokenParam.setRedirect_uri("http://localhost:8080/callback");
        String Token = gitHubProvider.getAccessToken(accessTokenParam);   //获取到access_token
        String accessToken = Token.split("&")[0].split("=")[1];
        //使用access_token获取用户信息
        GitHubUser gitHubUser = gitHubProvider.getGitHubUser(accessToken);
        System.out.println(gitHubUser.getName());
        return "index";
    }
}
