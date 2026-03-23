package com.banking.modules.identity.config;

import com.banking.modules.identity.entity.Permission;
import com.banking.modules.identity.entity.Role;
import com.banking.modules.identity.repository.PermissionRepository;
import com.banking.modules.identity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        // Essential permissions
        Permission accountReadOwn = createPermission
                ("ACCOUNT_READ_OWN", "Xem tài khoản cá nhân");
        Permission accountReadAll = createPermission
                ("ACCOUNT_READ_ALL", "Xem tất cả tài khoản hệ thống");

        Permission txCreateOwn = createPermission
                ("TRANSACTION_CREATE_OWN", "Tự tạo giao dịch chuyển tiền");
        Permission txCreateInternal = createPermission
                ("TRANSACTION_CREATE_INTERNAL", "Tạo giao dịch tại quầy (Maker)");
        Permission txApprove = createPermission
                ("TRANSACTION_APPROVE", "Duyệt giao dịch (Checker)");

        Permission userManage = createPermission
                ("USER_MANAGE", "Quản lý người dùng (Khóa/Mở khóa)");

        // Essential roles
        Set<Permission> customerPermissions = new HashSet<>();
        customerPermissions.add(accountReadOwn);
        customerPermissions.add(txCreateOwn);
        createRole("ROLE_CUSTOMER", "Khách hàng cá nhân", customerPermissions);

        Set<Permission> tellerPermissions = new HashSet<>();
        tellerPermissions.add(accountReadAll);
        tellerPermissions.add(txCreateInternal);
        createRole("ROLE_TELLER", "Giao dịch viên", tellerPermissions);

        Set<Permission> managerPermissions = new HashSet<>();
        managerPermissions.add(accountReadAll);
        managerPermissions.add(txApprove);
        createRole("ROLE_BRANCH_MANAGER", "Quản lý chi nhánh", managerPermissions);

        Set<Permission> adminPermissions = new HashSet<>();
        adminPermissions.add(userManage);
        adminPermissions.add(accountReadAll);
        createRole("ROLE_ADMIN", "Quản trị viên hệ thống", adminPermissions);

        log.info("Initial essential roles and permission completed");
        log.info("=======================================================================================");
        log.info(String.format("%-25s | %-55s", "ROLE CODE", "PERMISSIONS"));
        log.info("=======================================================================================");
        List<Role> roles = this.roleRepository.findAllWithPermissions();
        for(Role role : roles) {
            String permissionCode = role.getPermissions().stream()
                    .map(Permission::getCode)
                    .collect(Collectors.joining(", "));
            log.info(String.format("%-25s | %-55s", role.getCode(), permissionCode));
        }
        log.info("=======================================================================================");

    }

    private Permission createPermission(String code, String description) {
        return this.permissionRepository.findByCode(code).orElseGet(() -> {
            Permission permission = new Permission();
            permission.setCode(code);
            permission.setDescription(description);
            return permissionRepository.save(permission);
        });
    }

    private Role createRole(String code, String description, Set<Permission> permissions) {
        return this.roleRepository.findByCode(code).orElseGet(() -> {
           Role role = new Role();
           role.setCode(code);
           role.setDescription(description);
           role.setPermissions(permissions);
           return this.roleRepository.save(role);
        });
    }

}
