package com.example.finalproject_customer.adapters.orderstatusadapter;

public class OrderStatus {
    String jobName , orderNumber , orderDate;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus(String jobName, String orderNumber, String orderDate) {
        this.jobName = jobName;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
    }
}
