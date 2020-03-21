package wang.kingweb.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import wang.kingweb.community.dto.NotificationDTO;
import wang.kingweb.community.dto.PaginationDTO;
import wang.kingweb.community.enums.CustomizeErrorCode;
import wang.kingweb.community.enums.NotificationStatusEnum;
import wang.kingweb.community.exception.CustomizeException;
import wang.kingweb.community.mapper.ArticleMapper;
import wang.kingweb.community.mapper.NotificationMapper;
import wang.kingweb.community.model.Notification;
import wang.kingweb.community.model.NotificationExample;
import wang.kingweb.community.model.User;
import wang.kingweb.community.service.NotificationService;
import wang.kingweb.community.service.PaginationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private PaginationService paginationService;

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("/notification")
    public String getNotifications(Model model, HttpServletRequest request,
                                   @RequestParam(value = "page",defaultValue = "1") Long page,
                                   @RequestParam(value = "size",defaultValue = "5") Long size,
                                   @RequestParam(value = "search",required = false) String search){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NOT_LOGIN);
        }
        if(StringUtils.isBlank(search)){
            //查询所有消息
            NotificationExample notificationExample = new NotificationExample();
            notificationExample.createCriteria().andReceiverIdEqualTo(user.getId());
            long totalCount = notificationMapper.countByExample(notificationExample);
            PaginationDTO paginationDTO =  paginationService.getPageInfo(page,size,totalCount);
            List<NotificationDTO> notificationDTOList = notificationService.selectNotifications(user.getId(),paginationDTO.getOffset(),size);
            model.addAttribute("paginationInfo",paginationDTO);
            model.addAttribute("selection","notification");
            model.addAttribute("notifications",notificationDTOList);
            return "notification";
        }else {
            return "redirect:/?search="+search;
        }
    }

    @GetMapping("/notification/read/{nid}/{aid}")
    public String notificationReadHandle(@PathVariable("nid") Long nid,
                                         @PathVariable("aid") Long aid,
                                         HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NOT_LOGIN);
        }
        Notification selectNotification = notificationMapper.selectByPrimaryKey(nid);
        if(selectNotification==null){
            throw new CustomizeException(CustomizeErrorCode.FILE_NOT_FOUND);

        }else{
            //如果状态是未读，进行更新
            if(selectNotification.getStatus()==NotificationStatusEnum.NOTIFICATION_UNREAD.getStatus()){
                NotificationExample notificationExample = new NotificationExample();
                notificationExample.createCriteria().andIdEqualTo(nid);

                Notification notification = new Notification();
                notification.setStatus(NotificationStatusEnum.NOTIFICATION_READ.getStatus());
                notificationMapper.updateByExampleSelective(notification,notificationExample);
            }
            //判断改文章是否存在，不能存在则删除该消息并抛异常
            if(articleMapper.selectByPrimaryKey(aid)==null){
                notificationMapper.deleteByPrimaryKey(nid);
                throw new CustomizeException(CustomizeErrorCode.COMMENT_ARTICLE_ID_NOT_FOUND);
            }
            return "redirect:/article/detail/"+aid;
        }
    }
}
