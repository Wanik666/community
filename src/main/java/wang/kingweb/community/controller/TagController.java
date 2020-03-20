package wang.kingweb.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import wang.kingweb.community.dto.RespDTO;
import wang.kingweb.community.mapper.TagMapper;
import wang.kingweb.community.model.Tag;
import wang.kingweb.community.model.TagExample;

import java.util.List;

@Controller
public class TagController {

    @Autowired
    TagMapper tagMapper;

    @ResponseBody
    @GetMapping("/tag/{name}")
    public RespDTO tag(@PathVariable(name = "name") String name){
        if(!StringUtils.isBlank(name)){
            TagExample example = new TagExample();
            example.createCriteria().andTagNameEqualTo(name);
            List<Tag> tags = tagMapper.selectByExample(example);
            if(tags.size()>0){
                return RespDTO.ok("获取成功",true);
            }else {
                return RespDTO.error("不合法的标签！");
            }
        }else {
            return RespDTO.error("标签格式不正确！");
        }
    }
}
