package com.example.rqchallenge.service;

import com.example.rqchallenge.employees.model.EmployeResponse;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.config.EmployeeConstants;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    private List<Employee> mockEmployeeList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockEmployeeList = new ArrayList<>();
        mockEmployeeList.add(new Employee(1L, "John Doe", 125000,36,""));
        mockEmployeeList.add(new Employee(2L, "Mark Smith", 145000,40,""));
        mockEmployeeList.add(new Employee(3L, "Isaac Peguero", 130000,35,""));
        mockEmployeeList.add(new Employee(4L, "David Ramos", 150000,23,""));

        EmployeResponse<List<Employee>> mockResponse = new EmployeResponse<>();
        mockResponse.setData(mockEmployeeList);

        ResponseEntity<EmployeResponse<List<Employee>>> mockResponseEntities = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(EmployeeConstants.ALL_EMPLOYEES_URL),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponseEntities);
    }

    @Test
    public void testGetAllEmployees() {

        ResponseEntity<List<Employee>> responseEntity = employeeService.getAllEmployees();

        assertEquals(4, responseEntity.getBody().size());
        assertEquals("John Doe", responseEntity.getBody().get(0).getEmployeeName());
        assertEquals("David Ramos", responseEntity.getBody().get(3).getEmployeeName());
    }

    @Test
    public void testGetEmployeesByNameSearchPositive() {
        ResponseEntity<List<Employee>> responseEntity = employeeService.getEmployeesByNameSearch("Ra");

        assertEquals(1, responseEntity.getBody().size());
        assertEquals("David Ramos", responseEntity.getBody().get(0).getEmployeeName());
    }

    @Test
    public void testGetEmployeesByNameSearchNegative() {
        ResponseEntity<List<Employee>> responseEntity = employeeService.getEmployeesByNameSearch("Jesus");

        assertEquals(0, responseEntity.getBody().size());
    }

    @Test
    public void  testGetEmployeesByIdPositive() {
        Long id = 3L;
        Employee mockEmployee = mockEmployeeList.get(2);
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(mockEmployee, HttpStatus.OK);
        when(restTemplate.exchange(
                EmployeeConstants.GET_EMPLOYEE_URL + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Employee>() {}
        )).thenReturn(responseEntity);

        ResponseEntity<Employee> result = employeeService.getEmployeeById(id.toString());

        assertEquals(mockEmployee, result.getBody());
        assertEquals("Isaac Peguero", result.getBody().getEmployeeName());
    }

    @Test
    public void  testGetEmployeesByIdNegative() {
        Integer id = 2;
        Employee mockEmployee = mockEmployeeList.get(id-1);
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(mockEmployee, HttpStatus.OK);
        when(restTemplate.exchange(
                EmployeeConstants.GET_EMPLOYEE_URL + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Employee>() {}
        )).thenReturn(responseEntity);

        ResponseEntity<Employee> result = employeeService.getEmployeeById(id.toString());

        assertNotEquals("Isaac Peguero", result.getBody().getEmployeeName());
        assertNotNull(result.getBody());

    }


}
