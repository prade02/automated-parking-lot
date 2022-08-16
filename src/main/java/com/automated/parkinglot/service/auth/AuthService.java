package com.automated.parkinglot.service.auth;

import com.automated.parkinglot.models.auth.RoleHierarchy;
import com.automated.parkinglot.repository.auth.AuthServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final AuthServiceRepository authServiceRepository;

    @Override
    public RoleHierarchy getRoleHierarchy() {
        return authServiceRepository.getRoleHierarchyExpression();
    }
}
