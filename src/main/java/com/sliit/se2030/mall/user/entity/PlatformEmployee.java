package com.sliit.se2030.mall.user.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Instant;

@Entity
@DiscriminatorValue("PLATFORM_EMPLOYEE")
public class PlatformEmployee extends User {

    private String employeeCode;

    // When this employee last opened the pending shop approvals list. Shops that
    // registered after this time count as "new" for them -- each employee has
    // their own, so one admin checking the list doesn't clear it for the others.
    // Null means they've never looked.
    private Instant pendingShopsSeenAt;

    protected PlatformEmployee() {
        super();
    }

    public PlatformEmployee(String email, String passwordHash, String fullName) {
        super(email, passwordHash, fullName, Role.PLATFORM_EMPLOYEE);
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Instant getPendingShopsSeenAt() {
        return pendingShopsSeenAt;
    }

    public void setPendingShopsSeenAt(Instant pendingShopsSeenAt) {
        this.pendingShopsSeenAt = pendingShopsSeenAt;
    }
}
