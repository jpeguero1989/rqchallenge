package com.example.rqchallenge.employees.config;

/**
 * This interface defines all the constants for the Employee
 */
public interface EmployeeConstants {

     /**
      * Based URL where request will be sent
      */
     String BASE_URL = "https://dummy.restapiexample.com/api/v1";
     /**
      * URL that returns all the employees
      */
     String ALL_EMPLOYEES_URL = BASE_URL + "/employees";
     /**
      * URL that returns an employee
      */
     String GET_EMPLOYEE_URL = BASE_URL + "/employee/";
     /**
      * URL that allows to create an employee
      */
     String CREATE_EMPLOYEE_URL = BASE_URL + "/create";
     /**
      * URL that allows to delete an employee
      */
     String DELETE_EMPLOYEE_URL = BASE_URL + "/delete/";

}
