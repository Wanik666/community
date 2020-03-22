/*提交文章评论*/
function postComment() {
    var parentId = $("#parentId").val();
    var content = $("#commentArea").val();
    var data = {"parentId":parentId,"content":content,"type":1};
    if(content==undefined||content==""){
        /*swal("评论内容不能为空",{
            icon:"warning",
            buttons: false,
            timer: 1500,
        })*/
        toastr.warning("评论内容不能为空");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        data:JSON.stringify(data),
        dataType:"json",
        contentType:"application/json",
        success:function (result) {
            if(result.code==200){
                //$("#commentArea").val("");
                /*swal(result.message,{
                    icon:"success",
                    buttons: false,
                    timer: 1500,
                }).then(function () {
                    window.location.reload();
                })*/
                toastr.success(result.message,"",{
                    onHidden:function () {
                        window.location.reload();
                    }
                });
            }else {
                /*swal(result.message,{
                    icon:"error",
                    buttons: false,
                    timer: 1000,
                })*/
                toastr.error(result.message);

            }
        }
    });

}

function toLogin() {
    var path = window.location.href;
    window.localStorage.setItem("historyPath",path);
    window.location.href="/login";

}

function doLogin() {
    window.localStorage.setItem("toHistory","true");
}

function showChildComment(ele) {
    var parentId =$(ele).attr("data-id");
    $(ele).toggleClass("operateBgColor");
    var childComment = $("#comment-"+parentId);
    childComment.toggleClass("in ");
    var data = {childId:parentId};
    if(childComment.hasClass("in")){
        $.ajax({
            type:"get",
            url:"/childComment",
            data:data,
            dataType:"json",
            contentType:"application/json",
            success:function (result) {
                var obj = result.obj;
                $.each(obj,function (i, o) {
                    var time = new Date(o.createTime).format("yyyy-MM-dd");
                    pushCommentHtml(parentId,o.user.avatarUrl,o.user.name,o.content,time);
                });
            }
        });

    }else{
        $("#comment-"+parentId +" .comment-detail").remove()

    }

}

function subComment(ele) {
    var parentId = $(ele).attr("data-parentId");
    var subcomment = $("#childComment-"+parentId).val();
    if(subcomment==undefined||subcomment==null||subcomment==""){

        toastr.warning("评论内容不能为空");
        return;
    }

    var data = {"parentId":parentId,"content":subcomment,"type":2};

    $.ajax({
        type:"POST",
        url:"/comment",
        data:JSON.stringify(data),
        dataType:"json",
        contentType:"application/json",
        success:function (result) {
            if(result.code==200){
                obj = result.obj;
                $("#childComment-"+parentId).val("");
                var time = new Date().format("yyyy-MM-dd");
                pushCommentHtml(parentId,obj.avatarUrl,obj.name,subcomment,time);
                //伪评论数增加
                var commentCount = $("#commentCount-"+parentId);
                var commentCountVal  = commentCount.text();
                commentCountVal==''?commentCount.text(1):commentCount.text(parseInt(commentCountVal)+1);
                toastr.success(result.message);
                /*swal(result.message,{
                    icon:"success",
                    buttons: false,
                    timer: 1000,
                });*/
            }else {
                /*swal(result.message,{
                    icon:"error",
                    buttons: false,
                    timer: 1000,
                });*/
                toastr.error(result.message);
            }
        }
    });
}

function pushCommentHtml(id,imgurl,uname,content,time) {
    var html = "<div class='comment-detail col-lg-12 col-md-12 col-sm-12 col-xs-12'>" +
        "           <div class='comment-head'>" +
        "               <div class='media-left'>" +
        "                   <a href='#'>" +
        "                       <img class='img-rounded img-comment' src='" + imgurl + "'>" +
        "                   </a>" +
        "               </div>" +
        "               <div class='media-body'>" +
        "                   <span class='media-heading'>" + uname + "</span>" +
        "                   <span class='float-right'>" + time + "</span>"+
        "               </div>" +
        "           </div>" +
        "        <div class='comment-style comment-body'>" + content + "</div>" +
        "   </div>"

    var ele = $("#comment-" + id);
    ele.prepend(html);


}
//后续完善点赞功能，
/**
 * 思路：增加点赞表,id 主键，文章或者评论id ACid，type 类型，1文章：2评论
 *  第一次点击，增加数据到点赞表，第二次点赞，从点赞表删除点赞
 * */
function like(ele){
    var likeId = $(ele).attr("like-id");


}

var tagInput = undefined;

$(function() {
    toastr.options = {
        closeButton: false,
        debug: false,
        progressBar: false,
        positionClass: "toast-center-center",
        onclick: null,
        showDuration: "300",
        hideDuration: "1000",
        timeOut: "1500",
        extendedTimeOut: "1000",
        showEasing: "swing",
        hideEasing: "linear",
        showMethod: "fadeIn",
        hideMethod: "fadeOut"
    };



    tagInput = $("input[data-role=tagsinput]");

    tagInput.on('itemAdded',function (event) {
        var item = event.item;
        var _length =  $(this).val().split(",").length
        if(_length>=5){
            tagInput.tagsinput("remove",item);
            /*swal("已超过最大标签数5", {
                icon: "warning",
                buttons: false,
                timer: 1000,
            });*/
            toastr.warning("已超过最大标签数5");
            return;
        }

        $.ajax({
            url:"/tag/"+item,
            type:"get",
            dataType:"json",
            success:function (result) {
                if(result.code!==200){
                    tagInput.tagsinput("remove",item);
                    toastr.error(result.message);
                    /*swal(result.message,{
                        icon:"error",
                        buttons: false,
                        timer: 1000,
                    });*/
                }

            }
        })
    })
});

function addTag(e) {
    var tag = $(e).text();
    tagInput.tagsinput("add",tag+",");
}

function del(url) {
    swal({
        title: "确认删除文章吗？",
        text: "",
        icon: "warning",
        buttons: ["取消","删除"],
        dangerMode: true,
    }).then(function () {
        $.ajax({
            url:url,
            type:"delete",
            success:function (result) {
                if(result.code==200){
                    swal(result.message, {
                        icon: "success",
                        buttons: false,
                        timer: 1500,
                    }).then(function () {
                        window.location.reload();
                        //window.location.replace("/main/article");
                    })

                }else{
                    swal(result.message,{
                        icon:"error",
                        buttons: false,
                        timer: 1500,
                    });
                }
            }
        })
    })
}


function publish() {
    var data =  $("#publishForm").serializeObject();
    if(data.title===""||data.title==null){
        toastr.warning("文章标题不能为空~~")
        return;
    }if(data.description===""||data.description==null){
        toastr.warning("文章内容不能为空~~")
        return;
    }if(data.tag===""||data.tag==null){
        toastr.warning("标签不能为空~~")
        return;
    }
    swal({
        title: "确认发布文章吗？",
        text: "",
        icon: "warning",
        buttons: ["取消","发布"],
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {

                $.ajax({
                    url:"/publish",
                    type:"POST",
                    data:JSON.stringify(data),
                    dataType:"json",
                    contentType:"application/json",
                    success:function (result) {

                        if(result.code==200){
                            /*swal(result.message, {
                                icon: "success",
                                buttons: false,
                                timer: 1500,
                            }).then(function () {
                                window.location.replace("/");
                            })*/
                            toastr.success(result.message,"",{
                                onHidden:function () {
                                    window.location.replace("/");
                                }
                            });

                        }else{
                            toastr.error(result.message)
                            /*swal(result.message,{
                                icon:"error",
                                buttons: false,
                                timer: 1500,
                            });*/
                        }

                    }
                })

            } else {
            }
        });

}

$.prototype.serializeObject=function(){
    var obj=new Object();
    $.each(this.serializeArray(),function(index,param){
        if(!(param.name in obj)){
            obj[param.name]=param.value;
        }
    });
    return obj;
};


/**
 *对Date的扩展，将 Date 转化为指定格式的String
 *月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 *年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 *例子：
 *(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
 *(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
 */
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}