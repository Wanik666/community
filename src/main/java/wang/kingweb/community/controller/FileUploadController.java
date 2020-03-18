package wang.kingweb.community.controller;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wang.kingweb.community.dto.ImageUpLoadDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class FileUploadController {
    @RequestMapping("/img/upload")
    @ResponseBody
    public ImageUpLoadDTO imgUpload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file,
                                    HttpServletRequest request) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID()+suffix;
        String WebRoot = request.getScheme() +"://" + request.getServerName()
                + ":" +request.getServerPort()+"/upload/img/";
        String path = request.getSession().getServletContext().getRealPath("/upload/img/");

        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        ImageUpLoadDTO imageUpLoadDTO = new ImageUpLoadDTO();

        //保存
        try {
            file.transferTo(targetFile);
            imageUpLoadDTO.setSuccess(1);
            imageUpLoadDTO.setMessage("上传成功！");
            imageUpLoadDTO.setUrl(WebRoot+fileName);
            return imageUpLoadDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageUpLoadDTO.setSuccess(2);
        imageUpLoadDTO.setMessage("失败");
        imageUpLoadDTO.setUrl("");

        return imageUpLoadDTO;
    }
}
