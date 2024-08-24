package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.EmployeResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.example.rqchallenge.employees.config.EmployeeConstants;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import java.util.stream.Collectors;

/**
 * This Service class handle all the Employee fucnionalities
 */
@Service
public class EmployeeService {

    /**
     * Logger for logging info, warn or error.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    /**
     * RestTemplate Bean injection
     */
    @Autowired
    RestTemplate restTemplate;

    /**
     * This method returns a List of all employees
     *
     * @return ResponseEntity<List < Employee>>
     */
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            ResponseEntity<EmployeResponse<List<Employee>>> employeesList = restTemplate.exchange(
                    EmployeeConstants.ALL_EMPLOYEES_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
            });

            List<Employee> employees = employeesList.getBody().getData();

            LOGGER.info("Fetching employees");

            return new ResponseEntity<>(employees, employeesList.getStatusCode());

        } catch (Exception ex) {
            return exceptionHandling(ex, "Getting all employees");
        }
    }

    /**
     * This method returns all the employees that match their name with the
     * search string. Note: This is case sensitive
     *
     * @param searchString the String to filter the Employee name.
     * @return ResponseEntity<List < Employee>>
     */
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        try {
            ResponseEntity<List<Employee>> employeesResponse = getAllEmployees();

            if (employeesResponse.getStatusCode() == HttpStatus.OK && employeesResponse.getBody() != null) {

                List<Employee> filteredEmployees = employeesResponse.getBody().stream().filter(employee -> employee.getEmployeeName().contains(searchString)).collect(Collectors.toList());

                return new ResponseEntity<>(filteredEmployees, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception ex) {
            return exceptionHandling(ex, "getting employee by name");
        }
    }

    /**
     * This method returns the employee that match with the Id
     *
     * @param id the Id of the Employee to be retrieved.
     * @return ResponseEntity<Employee>
     */
    public ResponseEntity<Employee> getEmployeeById(String id) {
        try {
            ResponseEntity<Employee> employeeResponse = restTemplate.exchange(
                    EmployeeConstants.GET_EMPLOYEE_URL + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
            });

            Employee employee = employeeResponse.getBody();

            if (employee != null) {
                return new ResponseEntity<>(employee, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return exceptionHandling(ex, "getting highest salary of employees");
        }
    }

    /**
     * This method returns the Highest salary among the employees
     *
     * @return ResponseEntity<Integer>
     */
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            ResponseEntity<List<Employee>> employeesResponse = getAllEmployees();

            if (employeesResponse.getStatusCode() == HttpStatus.OK && employeesResponse.getBody() != null) {
                List<Employee> employees = employeesResponse.getBody();

                Employee highestPaidEmployee = employees.stream().max(Comparator.comparing(Employee::getEmployeeSalary)).orElse(null);

                if (highestPaidEmployee != null) {
                    return new ResponseEntity<>(highestPaidEmployee.getEmployeeSalary(), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return exceptionHandling(ex, "getting highest salary of employees");
        }
    }

    /**
     * This method returns a list of the top 10 employees based off of their salaries
     *
     * @return ResponseEntity<List < String>>
     */
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            ResponseEntity<List<Employee>> employees = getAllEmployees();

            List<String> top10EmployeeNames = employees.getBody().stream().sorted(Comparator.comparingDouble(Employee::getEmployeeSalary).reversed()).limit(10).map(Employee::getEmployeeName).collect(Collectors.toList());

            return new ResponseEntity<>(top10EmployeeNames, HttpStatus.OK);
        } catch (Exception ex) {
            return exceptionHandling(ex, "getting top 10 highest earning employees");
        }
    }

    /**
     * /**
     * This method deletes the employee with specified id given
     *
     * @param employeeInput Map with employee information.
     * @return ResponseEntity<Employee>
     */
    public ResponseEntity<String> createEmployee(Map<String, Object> employeeInput) {
        try {
            Employee newEmployee = mapToEmployee(employeeInput);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            HttpEntity<Employee> requestEntity = new HttpEntity<>(newEmployee, headers);

            ResponseEntity<Employee> responseEntity = restTemplate.exchange(EmployeeConstants.CREATE_EMPLOYEE_URL, HttpMethod.POST, requestEntity, Employee.class);

            return new ResponseEntity<>("Employee successfully created", responseEntity.getStatusCode());
        } catch (Exception ex) {
            return exceptionHandling(ex, "creating employee");
        }
    }

    /**
     * This method deletes the employee with specified id given
     *
     * @param id Employee id
     * @return ResponseEntity<String>
     */
    public ResponseEntity<String> deleteEmployeeById(String id) {
        try {
            restTemplate.delete(EmployeeConstants.DELETE_EMPLOYEE_URL + id);

            return new ResponseEntity<>("successfully! deleted Employee", HttpStatus.OK);
        } catch (Exception ex) {
            return exceptionHandling(ex, "deleting employee with id" + id);
        }

    }

    private Employee mapToEmployee(Map<String, Object> employeeInput) {
        Employee newEmployee = new Employee();

        newEmployee.setEmployeeName((String) employeeInput.get("employeeName"));
        newEmployee.setEmployeeSalary((int) employeeInput.get("employeeSalary"));
        newEmployee.setEmployeeAge((int) employeeInput.get("employeeAge"));

        return newEmployee;
    }

    /**
     * This method handles exception in one please for the whole class
     *
     * @param ex     the exception that was caught
     * @param action the action being performed when the exception occurred
     * @return ResponseEntity
     */
    private ResponseEntity exceptionHandling(Exception ex, String action) {
        LOGGER.error("An error occurred " + action, ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Default to 500

        if (ex instanceof HttpStatusCodeException) {
            status = ((HttpStatusCodeException) ex).getStatusCode();
        }

        return new ResponseEntity<>(status);
    }
}
