package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nnk.springboot.model.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

@Service
public class RuleNameService {

    private final RuleNameRepository ruleNameRepository;

    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    public List<RuleName> getAllRuleName() {
        return ruleNameRepository.findAll();
    }

    public void saveRuleName(RuleName rule) {
        ruleNameRepository.save(rule);
    }

    public void deleteRuleName(RuleName rule) {
        ruleNameRepository.delete(rule);
    }

    public Optional<RuleName> getRuleName(Integer id) {
        return ruleNameRepository.findById(id);
    }

}
