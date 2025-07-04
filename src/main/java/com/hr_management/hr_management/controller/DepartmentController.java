package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.DepartmentMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.DepartmentDTO;
import com.hr_management.hr_management.model.dto.DepartmentResponseDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.repository.LocationRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final LocationRepository locationRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentController(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, LocationRepository locationRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.locationRepository = locationRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllDepartments(HttpServletRequest request) {
         List<DepartmentDTO> departments = departmentRepository.findAll().stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());

        return BuildResponse.success(departments, "List of all Departments", request.getRequestURI());
    }

    @GetMapping("/{department_id}")
    public ResponseEntity<ApiResponseDto> getDepartmentById(@PathVariable("department_id") BigDecimal departmentId, HttpServletRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
        return BuildResponse.success(departmentMapper.toDTO(department), "Department details retrieved", request.getRequestURI());
    }

    @GetMapping("/by_location/{location_id}")
    public ResponseEntity<ApiResponseDto> getDepartmentsByLocation(@PathVariable("location_id") BigDecimal locationId, HttpServletRequest request) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location not found with ID: " + locationId);
        }

        List<DepartmentDTO> departments = departmentRepository.findByLocationLocationId(locationId).stream()
            .map(departmentMapper::toDTO)
            .collect(Collectors.toList());

        if (departments.isEmpty()) {
            throw new ResourceNotFoundException("No departments found for location ID: " + locationId);
        }

        return BuildResponse.success(departments, "List of departments for location ID: " + locationId, request.getRequestURI());
    }

    @GetMapping("/by_manager/{manager_id}")
    public ResponseEntity<ApiResponseDto> getDepartmentsByManager(@PathVariable("manager_id") BigDecimal managerId, HttpServletRequest request) {
        if (!employeeRepository.existsById(managerId)) {
            throw new ResourceNotFoundException("Manager not found with ID: " + managerId);
        }

        List<DepartmentDTO> departments = departmentRepository.findDepartmentsByManager_EmployeeId(managerId).stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());

        if (departments.isEmpty()) {
            throw new ResourceNotFoundException("No departments found for manager ID: " + managerId);
        }

        return BuildResponse.success(departments, "List of departments for manager ID: " + managerId, request.getRequestURI());
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createDepartment(@Valid @RequestBody DepartmentResponseDTO departmentResponseDTO, HttpServletRequest request) {
        var location = locationRepository.findById(departmentResponseDTO.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + departmentResponseDTO.getLocationId()));

        var manager = departmentResponseDTO.getManagerId() != null ?
                employeeRepository.findById(departmentResponseDTO.getManagerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + departmentResponseDTO.getManagerId()))
                : null;

        Department department = new Department();
        department.setDepartmentId(departmentResponseDTO.getDepartmentId());
        department.setDepartmentName(departmentResponseDTO.getDepartmentName());
        department.setLocation(location);
        department.setManager(manager);

        Department savedDepartment = departmentRepository.save(department);

        return BuildResponse.success(departmentMapper.toDTO(savedDepartment), "Department successfully created", request.getRequestURI());
    }

    @PutMapping("/{department_id}")
    public ResponseEntity<ApiResponseDto> updateDepartment(@PathVariable("department_id") BigDecimal departmentId, @Valid @RequestBody DepartmentResponseDTO departmentResponseDTO, HttpServletRequest request) {
        // First, find the existing department
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));

        var location = locationRepository.findById(departmentResponseDTO.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + departmentResponseDTO.getLocationId()));

        var manager = departmentResponseDTO.getManagerId() != null ?
                employeeRepository.findById(departmentResponseDTO.getManagerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + departmentResponseDTO.getManagerId()))
                : null;

        existingDepartment.setDepartmentName(departmentResponseDTO.getDepartmentName());
        existingDepartment.setLocation(location);
        existingDepartment.setManager(manager);

        Department updatedDepartment = departmentRepository.save(existingDepartment);

        return BuildResponse.success(departmentMapper.toDTO(updatedDepartment), "Department successfully updated", request.getRequestURI());
    }

}
