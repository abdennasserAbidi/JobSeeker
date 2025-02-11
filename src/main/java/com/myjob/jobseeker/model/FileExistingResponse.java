package com.myjob.jobseeker.model;

public class FileExistingResponse {
    private int id;
    private boolean isExisted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExisted() {
        return isExisted;
    }

    public void setExisted(boolean isExisted) {
        this.isExisted = isExisted;
    }
}
