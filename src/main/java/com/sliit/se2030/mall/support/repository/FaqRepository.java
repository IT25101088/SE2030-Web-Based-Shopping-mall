package com.sliit.se2030.mall.support.repository;

import com.sliit.se2030.mall.support.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findByPublishedTrue();
}
