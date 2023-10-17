package com.example.unsplash.sys.util;

public class ErrorObserver {

    private int errorCode;
    private String message;

    public ErrorObserver(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorObserver(int errorBody) {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
