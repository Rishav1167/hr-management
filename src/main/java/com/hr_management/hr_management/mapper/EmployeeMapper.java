package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.EmployeeDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) return null;

        String jobTitle = employee.getJob() != null ? employee.getJob().getJobTitle() : null;
        
        return new EmployeeDTO(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getHireDate(),
                jobTitle,
                employee.getDepartment() != null ? employee.getDepartment().getDepartmentId() : null,
                employee.getManager() != null ? employee.getManager().getEmployeeId() : null,
                employee.getCommissionPct()
        );
    }

    public Employee toEntity(EmployeeDTO dto, Job job, Department department, Employee manager) {
        if (dto == null) return null;

        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setHireDate(dto.getHireDate());
        employee.setJob(job);
        employee.setDepartment(department);
        employee.setManager(manager);
        employee.setCommissionPct(dto.getCommissionPct());

        return employee;
    }
}
