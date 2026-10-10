package com.sliit.se2030.mall.user.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PLATFORM_EMPLOYEE")
public class PlatformEmployee extends User {

    private String employeeCode;

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
}
