package com.sentinel.demo;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.sentinel.demo.controller.DemoController;
import com.sentinel.demo.service.impl.DemoServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        initFlowQPSRule();
    }


    public static void initFlowQPSRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule flowRule = new FlowRule(DemoController.RESOURCE_NAME);

        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setLimitApp("default");
        flowRule.setCount(2);
        rules.add(flowRule);

        FlowRule flowRule2 = new FlowRule(DemoController.RESOURCE_NAME_QUERY_USER_BY_ID);
        flowRule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule2.setLimitApp("default");
        flowRule2.setCount(2);
        rules.add(flowRule2);

        FlowRule flowRule3 = new FlowRule(DemoServiceImpl.RESOURCE_NAME_QUERY_USER_BY_NAME);
        flowRule3.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule3.setLimitApp("default");
        flowRule3.setCount(2);
        rules.add(flowRule3);

        FlowRuleManager.loadRules(rules);
    }

}
