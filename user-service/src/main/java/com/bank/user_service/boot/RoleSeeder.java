package com.bank.user_service.boot;


import com.bank.user_service.Permission.Entity.Permission;
import com.bank.user_service.Permission.Repository.PermissionRepository;
import com.bank.user_service.Role.Entity.Role;
import com.bank.user_service.Role.Repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleSeeder(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.loadRoles();
    }

    public void loadRoles() {
        // Define roles and their associated permissions
        Map<String, Set<String>> rolesWithPermissions = Map.of(
                "SUPER_ADMIN", Set.of("MANAGE_ADMINS", "VIEW_AUDIT_LOGS", "CONFIGURE_SYSTEM"),

                "ADMIN", Set.of("MANAGE_USERS", "CREATE_USERS", "DELETE_USERS", "ASSIGN_ROLES",
                        "VIEW_REPORTS", "APPROVE_LOANS", "MANAGE_TRANSACTIONS"),

                //"TELLER", Set.of("PROCESS_DEPOSITS", "PROCESS_WITHDRAWALS", "VIEW_CUSTOMER_ACCOUNTS"),

                "LOAN_OFFICER", Set.of("REVIEW_LOAN_APPLICATIONS","VIEW_CUSTOMER_ACCOUNTS", "APPROVE_LOANS", "REJECT_LOANS", "MANAGE_LOAN_ACCOUNTS"),

                //"CUSTOMER_SERVICE", Set.of("VIEW_CUSTOMER_ACCOUNTS", "ASSIST_CUSTOMERS", "RESOLVE_QUERIES"),

                "CUSTOMER", Set.of("VIEW_OWN_ACCOUNT", "TRANSFER_FUNDS","DEPOSIT_FUNDS","WITHDRAW_FUNDS" ,"APPLY_LOAN", "PAY_BILLS")
        );


        rolesWithPermissions.forEach((roleName, permissionNames) -> {
            Role role = roleRepository.findByName(roleName).orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName(roleName);
                return roleRepository.save(newRole);
            });

            Set<Permission> permissions = new HashSet<>();
            for (String permName : permissionNames) {
                Permission permission = permissionRepository.findByName(permName)
                        .orElseGet(() -> permissionRepository.save(new Permission(permName)));
                permissions.add(permission);
            }

            role.setPermissions(permissions);
            roleRepository.save(role);
        });
    }
}
