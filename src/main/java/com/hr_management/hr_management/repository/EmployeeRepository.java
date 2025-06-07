package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.dto.EmployeeDTO;
import com.hr_management.hr_management.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RepositoryRestResource(path = "employee")
public interface EmployeeRepository extends JpaRepository<Employee, BigDecimal> {
    List<Employee> findAllByHireDate(LocalDate hireDate);

    Employee readEmployeeByEmployeeId(BigDecimal employeeId);
}
