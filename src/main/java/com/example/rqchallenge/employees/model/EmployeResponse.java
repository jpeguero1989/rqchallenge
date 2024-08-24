package com.example.rqchallenge.employees.model;

public class EmployeResponse<T> {
    private String status;
    private T data;

    public EmployeResponse() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
