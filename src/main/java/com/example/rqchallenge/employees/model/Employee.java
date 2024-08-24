package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents an Employee
 */
public class Employee {

    /**
     * Id of the employee
     */
    private Long id;

    /**
     * Name of the employee
     */
    @JsonProperty("employee_name")
    private String employeeName;

    /**
     * Salary of the employee
     */
    @JsonProperty("employee_salary")
    private int employeeSalary;

    /**
     * Age of the employee
     */
    @JsonProperty("employee_age")
    private int employeeAge;

    /**
     * Profile Image of the employee
     */
    @JsonProperty("profile_image")
    private String profileImage;

    /**
     * Constructor for the employee
     * @param id of the employee
     * @param name of the employee
     * @param salary of the employee
     * @param age of the employee
     * @param profile_image of the employee
     */
    public Employee (Long id, String name, int salary, int age, String profile_image)
    {
        this.id = id;
        this.employeeName = name;
        this.employeeSalary = salary;
        this.employeeAge = age;
        this.profileImage = profile_image;
    }

    public Employee() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(int employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

