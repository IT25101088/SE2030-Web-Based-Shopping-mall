package com.sliit.se2030.mall.support.repository;

import com.sliit.se2030.mall.support.entity.Inquiry;
import com.sliit.se2030.mall.support.entity.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByCustomer_Id(Long customerId);

    List<Inquiry> findByStatus(InquiryStatus status);

    // Powers the employee's inquiry queue: OPEN and IN_PROGRESS, everything
    // that isn't RESOLVED yet.
    List<Inquiry> findByStatusNot(InquiryStatus status);
}
