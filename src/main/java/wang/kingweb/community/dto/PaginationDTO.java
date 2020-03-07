package wang.kingweb.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private boolean hasPrePage; //是否有上一页
    private boolean hasNextPage; //是否有下一页
    private boolean hasFirstPage;   //是否显示首页
    private boolean hasLastPage;    //是否显示尾页
    private int offset; //mysql分页语法中的偏移量
    private int page;   //页码
    private int size;   //每页显示数据数
    private int countPage;  //总页数
    private List<Integer> pageList = new ArrayList<>(); //分页列表中的页码

    //具体计算分页数据类，并设置值
    public void setPagination(int page, int size, int totalCount) {
        this.page = page;

        //计算总页数
        if(totalCount%size==0){
            countPage = totalCount/size;
        }else{
            countPage = totalCount/size +1;
        }
        if(totalCount==0){
            countPage = 0;
        }

        //限制页码范围，防止用户输入导致越界
        if(page<1||countPage==0){
            page = 1;
        }
        else if (page>countPage){
            page = countPage;
        }

        //显示的页码列表
        pageList.add(page);
        for(int i = 1;i<=3;i++){
            if(page-i>=1){
                pageList.add(0,page-i);
            }
            if(page+i<=countPage){
                pageList.add(page+i);
            }
        }

        //首页、尾页、上一页、下一页是否展示
        if(pageList.contains(1)){
            hasFirstPage = false;
        }else{
            hasFirstPage = true;
        }
        if(pageList.contains(countPage)){
            hasLastPage = false;
        }else {
            hasLastPage = true;
        }
        if(page!=1){
            hasPrePage = true;
        }else{
            hasPrePage = false;
        }
        if(page!=countPage){
            hasNextPage = true;
        }else {
            hasNextPage = false;
        }

        //用于数据库偏移量
        this.offset = (page-1)*size;

    }

}
