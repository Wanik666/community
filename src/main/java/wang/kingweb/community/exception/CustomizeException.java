package wang.kingweb.community.exception;

import wang.kingweb.community.enums.CustomizeErrorCode;

public class CustomizeException extends RuntimeException {
    private String message;
    public CustomizeException(CustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
