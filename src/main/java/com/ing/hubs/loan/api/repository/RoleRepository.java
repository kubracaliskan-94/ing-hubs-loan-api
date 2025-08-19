package com.ing.hubs.loan.api.repository;

import com.ing.hubs.loan.api.model.Role;
import com.ing.hubs.loan.api.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleType name);
}
