package wang.kingweb.community.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.kingweb.community.dto.GitHubUser;
import wang.kingweb.community.mapper.UserMapper;
import wang.kingweb.community.model.User;
import wang.kingweb.community.model.UserExample;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String createOrUpdateUser(GitHubUser gitHubUser){
        String token = UUID.randomUUID().toString();
        //根据accountid查询是否存在该用户
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(gitHubUser.getId());
        List<User> userList = userMapper.selectByExample(userExample);
        //如果存在更新token,用户名及修改时间
        if(userList.size()>0){
            User selectUser = userList.get(0);
            if(gitHubUser.getName()==null||gitHubUser.getName()==""){
                selectUser.setName("gid_"+gitHubUser.getId());
            }else if(gitHubUser.getName()!=null&&selectUser.getName()!=null&&!gitHubUser.getName().equals(selectUser.getName())){
                selectUser.setName(gitHubUser.getName());
            }
            if(!gitHubUser.getAvatarUrl().equals(selectUser.getAvatarUrl())){
                selectUser.setAvatarUrl(gitHubUser.getAvatarUrl());
            }
            selectUser.setToken(token);
            selectUser.setModifiedTime(System.currentTimeMillis());
            userMapper.updateByPrimaryKeySelective(selectUser);
        }else{
            //如果不存在添加新的用户信息
            User user = new User();
            if(StringUtils.isBlank(gitHubUser.getName())){
                user.setName("GitHub"+gitHubUser.getId());
            }else{
                user.setName(gitHubUser.getName());
            }
            user.setAccountId(gitHubUser.getId());
            user.setToken(token);
            user.setAvatarUrl(gitHubUser.getAvatarUrl());
            user.setCreateTime(System.currentTimeMillis());
            user.setModifiedTime(user.getCreateTime());
            userMapper.insert(user);
        }
        return token;

    }
}
