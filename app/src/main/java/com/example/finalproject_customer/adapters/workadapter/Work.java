package com.example.finalproject_customer.adapters.workadapter;

public class Work {
    String image;
    String description;
    String workId;

    public Work(String image, String description, String workId) {
        this.image = image;
        this.description = description;
        this.workId = workId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }
}
