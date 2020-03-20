package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.Param;
import wang.kingweb.community.model.Notification;

import java.util.List;

public interface NotificationExtMapper {
    List<Notification> selectNotificationByLimit(@Param("id") Long id,@Param("offset") Long offset,@Param("size") Long size);
}