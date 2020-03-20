package wang.kingweb.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wang.kingweb.community.dto.ImageUpLoadDTO;
import wang.kingweb.community.provider.UFileProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileUploadController {

    @Autowired
    private UFileProvider uFileProvider;

    @RequestMapping("/img/upload")
    @ResponseBody
    public ImageUpLoadDTO imgUpload(@RequestParam("editormd-image-file") MultipartFile file,
                                    HttpServletRequest request) throws IOException {

        /*String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID()+suffix;
        String WebRoot = request.getScheme() +"://" + request.getServerName()
                + ":" +request.getServerPort()+"/upload/img/";
        String path = request.getSession().getServletContext().getRealPath("/upload/img/");

        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }*/

        //MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //MultipartFile file = multipartRequest.getFile("editormd-image-file");
        //保存
        ImageUpLoadDTO imageUpLoadDTO = new ImageUpLoadDTO();
        try {
            String url = uFileProvider.UploadFile2UFile(file);
            if(url!=null){
                imageUpLoadDTO.setSuccess(1);
                imageUpLoadDTO.setMessage("上传成功！");
                imageUpLoadDTO.setUrl(url);
                return imageUpLoadDTO;
            }
            imageUpLoadDTO.setSuccess(2);
            imageUpLoadDTO.setMessage("失败");
        } catch (Exception e) {
            e.printStackTrace();
            return imageUpLoadDTO;
        }
        return imageUpLoadDTO;
    }
}
