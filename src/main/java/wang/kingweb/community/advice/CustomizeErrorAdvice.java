package wang.kingweb.community.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import wang.kingweb.community.dto.RespDTO;
import wang.kingweb.community.enums.CustomizeErrorCode;
import wang.kingweb.community.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//处理JSON数据或者服务异常报错，并返回显示到前端
@ControllerAdvice
public class CustomizeErrorAdvice {
    //拦截所有异常并处理
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response) {
        //如果异常类型为自定义异常
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            RespDTO respDTO;

            // 返回 JSON
            if (e instanceof CustomizeException) {
                respDTO = RespDTO.error(e.getMessage());
            } else {
                respDTO = RespDTO.error(CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(respDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        }else{
            if(e instanceof CustomizeException){
                model.addAttribute("errorMsg",e.getMessage());
            }else{
                //其他异常
                model.addAttribute("errorMsg",CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }
}
