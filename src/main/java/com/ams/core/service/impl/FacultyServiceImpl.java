package com.ams.core.service.impl;

import com.ams.core.dto.FacultyCreationRequest;
import com.ams.core.dto.FacultyProfileDto;
import com.ams.core.dto.FacultyUpdateRequest;
import com.ams.core.entity.Department;
import com.ams.core.entity.FacultyProfile;
import com.ams.core.entity.Role;
import com.ams.core.entity.User;
import com.ams.core.exception.BadRequestException;
import com.ams.core.exception.ResourceNotFoundException;
import com.ams.core.repository.DepartmentRepository;
import com.ams.core.repository.FacultyProfileRepository;
import com.ams.core.repository.UserRepository;
import com.ams.core.service.FacultyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public FacultyServiceImpl(FacultyProfileRepository facultyProfileRepository,
                              UserRepository userRepository,
                              DepartmentRepository departmentRepository,
                              PasswordEncoder passwordEncoder) {
        this.facultyProfileRepository = facultyProfileRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public FacultyProfileDto registerFaculty(FacultyCreationRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already taken");
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.departmentId()));

        // Create base credentials
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.FACULTY);
        user.setActive(true);

        // Create detailed faculty profile
        FacultyProfile facultyProfile = new FacultyProfile();
        facultyProfile.setUser(user);
        facultyProfile.setFirstName(request.firstName());
        facultyProfile.setLastName(request.lastName());
        facultyProfile.setPhone(request.phone());
        facultyProfile.setDesignation(request.designation());
        facultyProfile.setJoiningDate(LocalDate.now());
        facultyProfile.setDepartment(department);

        // Generate unique employee ID: FAC + Year + 4 digit sequence (e.g. FAC20260001)
        String prefix = "FAC" + LocalDate.now().getYear();
        Optional<FacultyProfile> lastFacultyOpt = facultyProfileRepository.findFirstByEmployeeIdStartingWithOrderByEmployeeIdDesc(prefix);
        int nextSequenceId = 1;
        if (lastFacultyOpt.isPresent()) {
            String lastEmployeeId = lastFacultyOpt.get().getEmployeeId();
            try {
                String sequencePart = lastEmployeeId.substring(prefix.length());
                nextSequenceId = Integer.parseInt(sequencePart) + 1;
            } catch (NumberFormatException e) {
                // Keep nextSequenceId = 1
            }
        }
        String employeeId = prefix + String.format("%04d", nextSequenceId);
        facultyProfile.setEmployeeId(employeeId);

        FacultyProfile savedProfile = facultyProfileRepository.save(facultyProfile);
        return convertToDto(savedProfile);
    }

    @Override
    @Transactional
    public FacultyProfileDto updateFaculty(Long id, FacultyUpdateRequest request) {
        FacultyProfile facultyProfile = facultyProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty member not found with ID: " + id));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.departmentId()));

        facultyProfile.setFirstName(request.firstName());
        facultyProfile.setLastName(request.lastName());
        facultyProfile.setPhone(request.phone());
        facultyProfile.setDesignation(request.designation());
        facultyProfile.setDepartment(department);

        FacultyProfile updatedProfile = facultyProfileRepository.save(facultyProfile);
        return convertToDto(updatedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyProfileDto getFacultyById(Long id) {
        FacultyProfile facultyProfile = facultyProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty member not found with ID: " + id));
        return convertToDto(facultyProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyProfileDto getFacultyByUsername(String username) {
        FacultyProfile facultyProfile = facultyProfileRepository.findByUserUsernameWithUserAndDepartment(username)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found for username: " + username));
        return convertToDto(facultyProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacultyProfileDto> searchAndListFaculty(String query, Pageable pageable) {
        Page<FacultyProfile> profiles;
        if (StringUtils.hasText(query)) {
            profiles = facultyProfileRepository.searchFaculty(query, pageable);
        } else {
            profiles = facultyProfileRepository.findAllWithUserAndDepartment(pageable);
        }
        return profiles.map(this::convertToDto);
    }

    @Override
    @Transactional
    public void toggleFacultyActiveStatus(Long id, boolean active) {
        FacultyProfile facultyProfile = facultyProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty member not found with ID: " + id));
        User user = facultyProfile.getUser();
        user.setActive(active);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteFaculty(Long id) {
        FacultyProfile facultyProfile = facultyProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty member not found with ID: " + id));
        
        // Cascades delete to User credentials table automatically
        facultyProfileRepository.delete(facultyProfile);
    }

    private FacultyProfileDto convertToDto(FacultyProfile profile) {
        return new FacultyProfileDto(
                profile.getId(),
                profile.getEmployeeId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getUser().getUsername(),
                profile.getUser().getEmail(),
                profile.getPhone(),
                profile.getDesignation(),
                profile.getJoiningDate(),
                profile.getDepartment().getId(),
                profile.getDepartment().getName(),
                profile.getUser().isActive()
        );
    }
}
