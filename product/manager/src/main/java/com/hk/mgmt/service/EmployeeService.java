package com.hk.mgmt.service;

import com.hk.mgmt.domain.Employee;
import com.hk.mgmt.domain.User;
import com.hk.mgmt.domain.UserRole;
import com.hk.mgmt.dto.employee.EmployeeCreateRequest;
import com.hk.mgmt.dto.employee.EmployeeDetailDto;
import com.hk.mgmt.dto.employee.EmployeeListDto;
import com.hk.mgmt.dto.employee.EmployeeUpdateRequest;
import com.hk.mgmt.event.UserCreatedEvent;
import com.hk.mgmt.repository.EmployeeRepository;
import com.hk.mgmt.repository.UserRepository;
import com.hk.mgmt.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private static final String DEFAULT_ROLE_ID = "c6794018-1239-4bfb-9017-2796bd802817";
    private static final String TEMP_PW_CHARS   = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";

    private final EmployeeRepository    employeeRepository;
    private final UserRepository        userRepository;
    private final UserRoleRepository    userRoleRepository;
    private final PasswordEncoder       passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    // ── 목록 ──────────────────────────────────────────────────────────

    public List<EmployeeListDto> getList(String status, String name,
                                         String employeeNo, String orgId) {
        String statusParam     = StringUtils.hasText(status)     ? status     : null;
        String nameParam       = StringUtils.hasText(name)       ? name       : null;
        String employeeNoParam = StringUtils.hasText(employeeNo) ? employeeNo : null;
        String orgIdParam      = StringUtils.hasText(orgId)      ? orgId      : null;

        List<Employee> employees;
        if (statusParam == null && nameParam == null && employeeNoParam == null && orgIdParam == null) {
            employees = employeeRepository.findAllByOrderByEmployeeNoAsc();
        } else {
            employees = employeeRepository.search(statusParam, nameParam, employeeNoParam, orgIdParam);
        }

        Set<String> linkedIds = new HashSet<>(userRepository.findAllLinkedEmployeeIds());
        return employees.stream()
                .map(e -> EmployeeListDto.from(e, linkedIds.contains(e.getId())))
                .toList();
    }

    // ── 상세 ──────────────────────────────────────────────────────────

    public EmployeeDetailDto getDetail(String id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
        var linkedUser = userRepository.findByEmployeeId(id).orElse(null);
        return EmployeeDetailDto.from(emp, linkedUser);
    }

    // ── 등록 ──────────────────────────────────────────────────────────

    @Transactional
    public EmployeeDetailDto create(EmployeeCreateRequest req) {
        if (employeeRepository.existsByEmployeeNo(req.employeeNo())) {
            throw new IllegalArgumentException("이미 사용 중인 사번입니다.");
        }

        String status   = StringUtils.hasText(req.status()) ? req.status() : "E";
        String empEmail = StringUtils.hasText(req.email())  ? req.email()  : null;

        Employee emp = Employee.builder()
                .employeeNo(req.employeeNo())
                .name(req.name())
                .orgId(StringUtils.hasText(req.orgId())      ? req.orgId()      : null)
                .position(StringUtils.hasText(req.position()) ? req.position()  : null)
                .mobile(StringUtils.hasText(req.mobile())    ? req.mobile()     : null)
                .phone(StringUtils.hasText(req.phone())      ? req.phone()      : null)
                .email(empEmail)
                .photo(StringUtils.hasText(req.photo())      ? req.photo()      : null)
                .startDate(req.startDate())
                .endDate(StringUtils.hasText(req.endDate())  ? req.endDate()    : null)
                .status(status)
                .build();

        employeeRepository.save(emp);

        User createdUser = null;
        if (empEmail != null && userRepository.findByEmail(empEmail).isEmpty()) {
            createdUser = createUserForEmployee(emp, empEmail, req.name());
        }

        return EmployeeDetailDto.from(emp, createdUser);
    }

    private User createUserForEmployee(Employee emp, String email, String name) {
        String tempPassword = generateTempPassword();
        User user = User.createWithTempPassword(email, name, passwordEncoder.encode(tempPassword), emp);
        userRepository.save(user);

        userRoleRepository.save(UserRole.builder()
                .userId(user.getId())
                .roleId(DEFAULT_ROLE_ID)
                .build());

        eventPublisher.publishEvent(new UserCreatedEvent(user.getId(), email, name, tempPassword));
        return user;
    }

    private String generateTempPassword() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(TEMP_PW_CHARS.charAt(rnd.nextInt(TEMP_PW_CHARS.length())));
        }
        return sb.toString();
    }

    // ── 수정 ──────────────────────────────────────────────────────────

    @Transactional
    public EmployeeDetailDto update(String id, EmployeeUpdateRequest req) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));

        String status = StringUtils.hasText(req.status()) ? req.status() : emp.getStatus();
        emp.update(
                req.name(),
                StringUtils.hasText(req.orgId())    ? req.orgId()    : null,
                StringUtils.hasText(req.position()) ? req.position() : null,
                StringUtils.hasText(req.mobile())   ? req.mobile()   : null,
                StringUtils.hasText(req.phone())    ? req.phone()    : null,
                StringUtils.hasText(req.email())    ? req.email()    : null,
                StringUtils.hasText(req.photo())    ? req.photo()    : null,
                StringUtils.hasText(req.endDate())  ? req.endDate()  : null,
                status,
                null
        );

        var linkedUser = userRepository.findByEmployeeId(id).orElse(null);
        return EmployeeDetailDto.from(emp, linkedUser);
    }

    // ── 삭제 (소프트 삭제 — 퇴직 처리) ───────────────────────────────

    @Transactional
    public void delete(String id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
        emp.changeStatus("D");
    }
}