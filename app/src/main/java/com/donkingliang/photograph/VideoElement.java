package com.donkingliang.photograph;

public class VideoElement {

    private int primary_id;
    private String task_id;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getPrimary_id() {
        return primary_id;
    }

    public void setPrimary_id(int primary_id) {
        this.primary_id = primary_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public VideoElement(int primary_id, String task_id,String status) {
        this.primary_id = primary_id;
        this.task_id = task_id;
        this.status = status;
    }


}
