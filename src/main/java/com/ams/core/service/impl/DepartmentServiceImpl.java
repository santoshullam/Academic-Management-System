package com.ams.core.service.impl;

import com.ams.core.dto.DepartmentDto;
import com.ams.core.dto.DepartmentRequest;
import com.ams.core.entity.Department;
import com.ams.core.exception.BadRequestException;
import com.ams.core.exception.ResourceNotFoundException;
import com.ams.core.repository.DepartmentRepository;
import com.ams.core.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public DepartmentDto createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByCode(request.code())) {
            throw new BadRequestException("Department code is already taken");
        }

        Department department = new Department();
        department.setName(request.name());
        department.setCode(request.code());

        Department saved = departmentRepository.save(department);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        // Check if code changes and is already taken
        if (!department.getCode().equalsIgnoreCase(request.code()) && departmentRepository.existsByCode(request.code())) {
            throw new BadRequestException("Department code is already taken");
        }

        department.setName(request.name());
        department.setCode(request.code());

        Department updated = departmentRepository.save(department);
        return convertToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return convertToDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentDto convertToDto(Department department) {
        return new DepartmentDto(
                department.getId(),
                department.getName(),
                department.getCode()
        );
    }
}
