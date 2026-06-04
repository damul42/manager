package com.hk.mgmt.service;

import com.hk.mgmt.domain.Employee;
import com.hk.mgmt.domain.User;
import com.hk.mgmt.domain.UserRole;
import com.hk.mgmt.dto.role.RoleListDto;
import com.hk.mgmt.dto.user.UserCreateRequest;
import com.hk.mgmt.dto.user.UserDetailDto;
import com.hk.mgmt.dto.user.UserListDto;
import com.hk.mgmt.dto.user.UserUpdateRequest;
import com.hk.mgmt.repository.EmployeeRepository;
import com.hk.mgmt.repository.RoleRepository;
import com.hk.mgmt.repository.UserRepository;
import com.hk.mgmt.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository     userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder    passwordEncoder;
    private final RoleRepository     roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final MenuService        menuService;

    // ── 목록 ──────────────────────────────────────────────────────────

    public List<UserListDto> getList(String email, String name, String status) {
        String emailParam  = StringUtils.hasText(email)  ? email  : null;
        String nameParam   = StringUtils.hasText(name)   ? name   : null;
        String statusParam = StringUtils.hasText(status) ? status : null;

        if (emailParam == null && nameParam == null && statusParam == null) {
            return userRepository.findAllWithEmployee()
                    .stream().map(UserListDto::from).toList();
        }
        //return userRepository.search(emailParam, nameParam, statusParam)
        //        .stream().map(UserListDto::from).toList();

        List<UserListDto> userList = userRepository.search(emailParam, nameParam, statusParam)
                .stream().map(UserListDto::from).toList();

        return userList;
    }

    // ── 상세 ──────────────────────────────────────────────────────────

    public UserDetailDto getDetail(String id) {
        return userRepository.findWithEmployeeById(id)
                .map(UserDetailDto::from)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    // ── 수정 ──────────────────────────────────────────────────────────

    @Transactional
    public UserDetailDto update(String id, UserUpdateRequest req) {
        var user = userRepository.findWithEmployeeById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.updateProfile(req.name(), req.status());

        if (StringUtils.hasText(req.newPassword())) {
            if (!req.newPassword().equals(req.confirmPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            user.changePassword(passwordEncoder.encode(req.newPassword()));
        }

        return UserDetailDto.from(user);
    }

    // ── 등록 ──────────────────────────────────────────────────────────

    @Transactional
    public UserDetailDto create(UserCreateRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (!req.password().equals(req.confirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Employee employee = null;
        if (StringUtils.hasText(req.employeeId())) {
            employee = employeeRepository.findById(req.employeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + req.employeeId()));
        }

        String status = StringUtils.hasText(req.status()) ? req.status() : "E";
        User user = User.create(req.email(), req.name(), passwordEncoder.encode(req.password()), status, employee);
        userRepository.save(user);
        return UserDetailDto.from(user);
    }

    // ── 역할 조회 ──────────────────────────────────────────────────────

    public List<RoleListDto> getUserRoles(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }
        List<String> roleIds = userRoleRepository.findAllByUserId(userId)
                .stream().map(UserRole::getRoleId).toList();
        if (roleIds.isEmpty()) return List.of();
        return roleRepository.findAllById(roleIds)
                .stream()
                .map(RoleListDto::from)
                .sorted(Comparator.comparing(RoleListDto::name))
                .toList();
    }

    // ── 역할 수정 ──────────────────────────────────────────────────────

    @Transactional
    public List<RoleListDto> updateUserRoles(String userId, List<String> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        userRoleRepository.deleteAllByUserId(userId);

        List<RoleListDto> result;
        if (roleIds == null || roleIds.isEmpty()) {
            result = List.of();
        } else {
            result = roleRepository.findAllById(roleIds).stream()
                    .peek(role -> userRoleRepository.save(
                            UserRole.builder().userId(userId).roleId(role.getId()).build()
                    ))
                    .map(RoleListDto::from)
                    .sorted(Comparator.comparing(RoleListDto::name))
                    .toList();
        }

        menuService.invalidateAndRebuild(userId, user.getEmail());
        return result;
    }

    // ── 삭제 (소프트 삭제) ─────────────────────────────────────────────

    @Transactional
    public void delete(String id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        user.changeStatus("D");
    }
}