package com.sliit.se2030.mall.support.service;

import com.sliit.se2030.mall.support.entity.FAQ;
import com.sliit.se2030.mall.support.repository.FaqRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Simple CRUD, lowest priority within this module per the spec.
@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FAQ> listAll() {
        return faqRepository.findAll();
    }
}
