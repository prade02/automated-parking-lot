package com.automated.parkinglot.repository.auth;

import com.automated.parkinglot.models.auth.RoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthServiceRepository extends JpaRepository<RoleHierarchy, String> {

    @Query(value = "CALL sp_get_role_hierarchy_expression(null)", nativeQuery = true)
    RoleHierarchy getRoleHierarchyExpression();
}
