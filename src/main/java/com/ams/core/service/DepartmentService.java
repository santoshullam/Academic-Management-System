package com.ams.core.service;

import com.ams.core.dto.DepartmentDto;
import com.ams.core.dto.DepartmentRequest;

import java.util.List;

public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentRequest request);
    DepartmentDto updateDepartment(Long id, DepartmentRequest request);
    DepartmentDto getDepartmentById(Long id);
    List<DepartmentDto> getAllDepartments();
    void deleteDepartment(Long id);
}
