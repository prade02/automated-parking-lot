package com.automated.parkinglot.models.auth;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class RoleHierarchy {
    @Id
    private String roleHierarchyExpression;
}
