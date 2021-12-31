package com.itelesoft.test.app.dtos;

public class ProcessResult<T> {

    private String action, processStatus, message;
    private T result;

    public ProcessResult() {
    }

    public ProcessResult(String action, String processStatus, String message, T result) {
        this.action = action;
        this.processStatus = processStatus;
        this.message = message;
        this.result = result;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
