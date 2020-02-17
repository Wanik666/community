package wang.kingweb.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.dto.AccessTokenParam;
import wang.kingweb.community.dto.GitHubUser;
import wang.kingweb.community.entity.User;
import wang.kingweb.community.mapper.UserMapper;
import wang.kingweb.community.provider.GitHubProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizationController {
    @Autowired
    GitHubProvider gitHubProvider;
    @Autowired
    UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){

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
        if(gitHubUser!=null){
            String token = UUID.randomUUID().toString();
            //根据accountid查询是否存在该用户
            User selectUser =  userMapper.selectUserByAccountId(gitHubUser.getId());

            //如果存在更新token,用户名及修改时间
            if(selectUser!=null){
                if(gitHubUser.getName()!=null&&selectUser.getName()!=null&&!gitHubUser.getName().equals(selectUser.getName())){
                    selectUser.setName(gitHubUser.getName());
                }
                selectUser.setToken(token);
                selectUser.setModifiedTime(System.currentTimeMillis());
                userMapper.updateUser(selectUser);
            }else{
                //如果不存在添加新的用户信息
                User user = new User();
                user.setName(gitHubUser.getName());
                user.setAccountId(gitHubUser.getId());
                user.setToken(token);
                user.setCreateTime(System.currentTimeMillis());
                user.setModifiedTime(user.getCreateTime());

                userMapper.addUser(user);
            }
            response.addCookie(new Cookie("token",token));
        }
        System.out.println(gitHubUser.getName());
        return "redirect:/";
    }
}
