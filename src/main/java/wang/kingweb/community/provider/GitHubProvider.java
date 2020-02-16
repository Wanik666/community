package wang.kingweb.community.provider;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import wang.kingweb.community.entity.AccessTokenParam;
import wang.kingweb.community.entity.GitHubUser;

import java.io.IOException;

@Component
public class GitHubProvider {
    //获取accessToken
    public String getAccessToken(AccessTokenParam accessTokenParam){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenParam), mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String accessonToken = response.body().string();
            return accessonToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    //用来获取github用户信息
    public GitHubUser getGitHubUser(String access_token){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+access_token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
