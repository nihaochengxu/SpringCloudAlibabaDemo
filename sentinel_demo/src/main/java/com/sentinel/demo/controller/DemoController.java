package com.sentinel.demo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sentinel.demo.entity.User;
import com.sentinel.demo.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Brave
 * @version V1.0
 * @date 2021/8/27
 */
@RequestMapping("/user")
@RestController
public class DemoController {
    //资源名称
    public static final String RESOURCE_NAME = "userList";
    public static final String RESOURCE_NAME_QUERY_USER_BY_ID = "queryUserById";

    @Resource
    private DemoService demoService;

    @GetMapping("/list")
    public List<User> getUserList() {
        List<User> userList = null;
        Entry entry = null;
        try {
            // 被保护的业务逻辑
            entry = SphU.entry(RESOURCE_NAME);
            userList = demoService.getList();
        } catch (BlockException e) {
            // 资源访问阻止，被限流或被降级
            return Collections.singletonList(new User("xxx", "资源访问被限流", 0));
        } catch (Exception e) {
            // 若需要配置降级规则，需要通过这种方式记录业务异常
            Tracer.traceEntry(e, entry);
        } finally {
            // 务必保证 exit，务必保证每个 entry 与 exit 配对
            if (entry != null) {
                entry.exit();
            }
        }
        return userList;
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable String id){
        if (SphO.entry(RESOURCE_NAME_QUERY_USER_BY_ID)) {
            try {
                //被保护的逻辑
                //模拟数据库查询数据
                return new User(id, "Tom", 25);
            } finally {
                //关闭资源
                SphO.exit();
            }
        } else {
            //资源访问阻止，被限流或被降级
            return "Resource is Block!!!";
        }
    }

    @GetMapping("/name/{name}")
    public Object getByName(@PathVariable String name) {
        return demoService.queryByUserName(name);
    }

    @GetMapping("/age")
    public Object getByAge() {
        return demoService.queryByAge(18);
    }

}
