package com.sliit.se2030.mall.user.repository;

import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformEmployeeRepository extends JpaRepository<PlatformEmployee, Long> {
}
