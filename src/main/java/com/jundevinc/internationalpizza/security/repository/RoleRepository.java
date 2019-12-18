package com.jundevinc.internationalpizza.security.repository;

import com.jundevinc.internationalpizza.security.model.Role;
import com.jundevinc.internationalpizza.security.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByUserRole(Roles role);
}
