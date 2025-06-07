package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.EmployeeMapper;
import com.hr_management.hr_management.model.dto.CountryDTO;
import com.hr_management.hr_management.model.dto.EmployeeDTO;
import com.hr_management.hr_management.model.dto.LocationDTO;
import com.hr_management.hr_management.model.entity.Countries;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.repository.EmployeeRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    EmployeeRepository employeeRepository;
    EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    // Get all employees
    @GetMapping("employees")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());

    }
    // Get employees by hire date
    @GetMapping("employees/{hireDate}")
    public List<EmployeeDTO> getEmployeeByHireDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDate) {
        return employeeRepository.findAllByHireDate(hireDate).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }
    // Get employee by ID
    @GetMapping("employees/id/{employee_id}")
    public EmployeeDTO getEmployeeById(@PathVariable BigDecimal employee_id) {
        return employeeMapper.toDTO(
                employeeRepository.readEmployeeByEmployeeId(employee_id));

    }
}
