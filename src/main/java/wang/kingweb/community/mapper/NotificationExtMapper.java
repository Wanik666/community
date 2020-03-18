package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import wang.kingweb.community.model.Notification;
import wang.kingweb.community.model.NotificationExample;

import java.util.List;

public interface NotificationExtMapper {
    List<Notification> selectNotificationByLimit(@Param("id") Long id,@Param("offset") Long offset,@Param("size") Long size);
}