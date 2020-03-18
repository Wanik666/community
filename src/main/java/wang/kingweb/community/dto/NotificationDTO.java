package wang.kingweb.community.dto;

import lombok.Data;
import wang.kingweb.community.model.Notification;

@Data
public class NotificationDTO extends Notification {
    private String userName;
    private String info;
    private boolean isRead;
}
