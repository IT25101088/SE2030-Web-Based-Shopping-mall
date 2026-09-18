package com.sliit.se2030.mall.support.service;

import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.support.dto.FaqForm;
import com.sliit.se2030.mall.support.entity.FAQ;
import com.sliit.se2030.mall.support.repository.FaqRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FAQ> listAll() {
        return faqRepository.findAll();
    }

    public List<FAQ> listPublished() {
        return faqRepository.findByPublishedTrue();
    }

    public FAQ getById(Long faqId) {
        return faqRepository.findById(faqId)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found: " + faqId));
    }

    public FAQ create(FaqForm form) {
        FAQ faq = new FAQ(form.getQuestion(), form.getAnswer());
        faq.setCategory(form.getCategory());
        return faqRepository.save(faq);
    }

    @Transactional
    public void update(Long faqId, FaqForm form) {
        FAQ faq = getById(faqId);
        faq.setQuestion(form.getQuestion());
        faq.setAnswer(form.getAnswer());
        faq.setCategory(form.getCategory());
    }

    @Transactional
    public void publish(Long faqId) {
        getById(faqId).setPublished(true);
    }

    @Transactional
    public void unpublish(Long faqId) {
        getById(faqId).setPublished(false);
    }

    public void delete(Long faqId) {
        faqRepository.delete(getById(faqId));
    }
}
