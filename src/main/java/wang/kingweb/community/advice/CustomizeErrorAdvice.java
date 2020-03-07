package wang.kingweb.community.advice;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import wang.kingweb.community.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
//处理JSON数据或者服务异常报错，并返回显示到前端
@ControllerAdvice
public class CustomizeErrorAdvice {
    //拦截所有异常并处理
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model) {
        //如果异常类型为自定义异常
        if(e instanceof CustomizeException){
            model.addAttribute("errorMsg",e.getMessage());
        }else{
            //其他异常
            model.addAttribute("errorMsg","服务器出错了，过一会再试试吧");
        }
        return new ModelAndView("error");
    }

}
