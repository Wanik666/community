package wang.kingweb.community.dto;

import lombok.Data;

@Data
public class RespDTO {
    private Integer code;
    private String message;
    private Object obj;

    private RespDTO() {
    }

    private RespDTO(Integer code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public static RespDTO ok(String message){
        return new RespDTO(200,message,null);
    }
    public static RespDTO ok(String message,Object obj){
        return new RespDTO(200,message,obj);
    }
    public static RespDTO error(String message){
        return new RespDTO(500,message,null);
    }
    public static RespDTO error(String message,Object obj){
        return new RespDTO(500,message,obj);
    }

}
