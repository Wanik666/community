package wang.kingweb.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wang.kingweb.community.enums.NotificationStatusEnum;
import wang.kingweb.community.mapper.NotificationMapper;
import wang.kingweb.community.mapper.UserMapper;
import wang.kingweb.community.model.NotificationExample;
import wang.kingweb.community.model.User;
import wang.kingweb.community.model.UserExample;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Value("${github.client.id}")
    private String clientId;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;
    //preHandle方法是在请求之前执行（Controller之前）
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.getServletContext().setAttribute("redirectUri",redirectUri);
        request.getServletContext().setAttribute("clientId",clientId);
        //处理用户session验证，如果用户没登录，验证不通过，不允许访问，反之可以访问
        User user;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for (Cookie cookie:cookies){
                if("token".equals(cookie.getName())){
                    String token = cookie.getValue();

                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> userList = userMapper.selectByExample(userExample);
                    if(userList.size()>0){
                        user = userList.get(0);
                        request.getSession().setAttribute("user",user);

                        //获取当前用户消息
                        NotificationExample notificationExample = new NotificationExample();
                        notificationExample.createCriteria()
                                .andReceiverIdEqualTo(user.getId())
                                .andStatusEqualTo(NotificationStatusEnum.NOTIFICATION_UNREAD.getStatus());
                        long count = notificationMapper.countByExample(notificationExample);
                        request.getSession().setAttribute("notification",count);


                    }

                }
            }
        }
        return true;
    }

    //postHandle方法在preHandle方法返回true后执行（Controller之后）
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //afterCompletion方法也是在preHandle方法返回true后执行（整个请求完成之后）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
