package wang.kingweb.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.kingweb.community.dto.NotificationDTO;
import wang.kingweb.community.enums.NotificationInfoEnum;
import wang.kingweb.community.enums.NotificationStatusEnum;
import wang.kingweb.community.mapper.NotificationExtMapper;
import wang.kingweb.community.mapper.UserMapper;
import wang.kingweb.community.model.Notification;
import wang.kingweb.community.model.User;
import wang.kingweb.community.model.UserExample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationExtMapper notificationExtMapper;

    @Autowired
    private UserMapper userMapper;



    public List<NotificationDTO> selectNotifications(Long id,Long offset,Long size) {


        List<Notification> notifications = notificationExtMapper.selectNotificationByLimit(id,offset,size);
        if(notifications.size()>0){
            List<NotificationDTO> notificationDTOList = new ArrayList<>();
            //获取所有用户id并去重
            Set<Long> senderIds = notifications.stream().map(n -> n.getSenderId()).collect(Collectors.toSet());
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdIn(new ArrayList<>(senderIds));
            List<User> userList = userMapper.selectByExample(userExample);
            //处理查询到的用户List转Map
            Map<Long, String> userMap = userList.stream().collect(Collectors.toMap(User::getId, User::getName));
            notifications.stream().forEach(notification -> {
                NotificationDTO notificationDTO = new NotificationDTO();
                BeanUtils.copyProperties(notification,notificationDTO);
                notificationDTO.setUserName(userMap.get(notification.getSenderId()));
                //true为已读，false为未读
                notificationDTO.setRead(NotificationStatusEnum.NOTIFICATION_READ.getStatus()==notification.getStatus());
                notificationDTO.setInfo(NotificationInfoEnum.ARTICLE.getType()==notification.getType()?NotificationInfoEnum.ARTICLE.getMessage():NotificationInfoEnum.COMMENT.getMessage());
                notificationDTOList.add(notificationDTO);
            });
            return notificationDTOList;
        }else{
            return new ArrayList<>();
        }



    }
}
