package wang.kingweb.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class UFileProvider {

    @Value("${UCloud.UFile.publicKey}")
    private String publicKey;
    @Value("${UCloud.UFile.privateKey}")
    private String privateKey;
    @Value("${UCloud.UFile.config-region}")
    private String region;
    @Value("${UCloud.UFile.config-suffix}")
    private String suffix;
    @Value("${UCloud.UFile.bucket-name}")
    private String bucketName;
    @Value("${UCloud.UFile.expires}")
    private int expires;

    public String UploadFile2UFile(MultipartFile file){
        String fileName;
        ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(
                publicKey, privateKey);
        ObjectConfig config = new ObjectConfig(region, suffix);
        String[] filePaths = file.getOriginalFilename().split("\\.");
        if(filePaths.length>1){
            fileName = UUID.randomUUID().toString()+"."+filePaths[filePaths.length - 1];
        }else{
            return null;
        }
        try {
            PutObjectResultBean response = UfileClient.object(OBJECT_AUTHORIZER, config)
                    .putObject(file.getInputStream(), file.getContentType())
                    .nameAs(fileName)
                    .toBucket(bucketName)
                    .setOnProgressListener(( bytesWritten,contentLength)-> {})
                    .execute();
            if (response != null && response.getRetCode() == 0) {
                String url = UfileClient.object(OBJECT_AUTHORIZER, config)
                        .getDownloadUrlFromPrivateBucket(fileName, bucketName, expires)
                        .createUrl();
                return url;
            }
        } catch (UfileClientException e) {
            e.printStackTrace();
        } catch (UfileServerException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
