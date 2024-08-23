package com.nnk.springboot.unit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.model.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;

@ExtendWith(MockitoExtension.class)
public class RuleServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    public RuleNameService ruleNameService;

    RuleName defaultRuleName;

    @BeforeEach
    public void setup() {
        defaultRuleName = new RuleName("ruleName", "description", "json", "template", "sqlStr", "sqlPart");
        ruleNameService = new RuleNameService(ruleNameRepository);
    }

    @Test
    public void getAllRuleNameTest() {
        List<RuleName> allRuleName = new ArrayList<RuleName>();
        allRuleName.add(defaultRuleName);

        when(ruleNameRepository.findAll()).thenReturn(allRuleName);
        ruleNameService.getAllRuleName();

        verify(ruleNameRepository).findAll();
    }

    @Test
    public void getRuleNameTest() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(defaultRuleName));
        ruleNameService.getRuleName(1);

        verify(ruleNameRepository).findById(1);
    }

    @Test
    public void saveRuleNameTest() {
        ruleNameService.saveRuleName(defaultRuleName);

        verify(ruleNameRepository).save(defaultRuleName);
    }

    @Test
    public void deleteRuleNameTest() {
        ruleNameService.deleteRuleName(defaultRuleName);

        verify(ruleNameRepository).delete(defaultRuleName);
    }

}
