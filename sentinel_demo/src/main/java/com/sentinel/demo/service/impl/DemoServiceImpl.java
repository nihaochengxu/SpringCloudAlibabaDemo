package com.sentinel.demo.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sentinel.demo.entity.User;
import com.sentinel.demo.service.DemoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brave
 * @version V1.0
 * @date 2021/8/27
 */
@Service
public class DemoServiceImpl implements DemoService {

    //资源名称
    public static final String RESOURCE_NAME_QUERY_USER_BY_NAME = "queryUserByUserName";
    public static final String RESOURCE_NAME_QUERY_USER_BY_AGE= "getByAge";

    @Override
    public List<User> getList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("1", "周慧敏", 18));
        userList.add(new User("2", "关之琳", 20));
        userList.add(new User("3", "王祖贤", 21));
        return userList;
    }

    //value是资源名称，是必填项。blockHandler填限流处理的方法名称
    @Override
    @SentinelResource(value = RESOURCE_NAME_QUERY_USER_BY_NAME, blockHandler = "queryUserByUserNameBlock", fallback = "queryUserByUserNameFallBack")
    public User queryByUserName(String userName) {
        if (userName == null || "1".equals(userName)) {
            //抛出异常
            throw new RuntimeException("queryByUserName() command failed, userName is null");
        }
        return new User("0", userName, 18);
    }

    //注意细节，一定要跟原函数的返回值和形参一致，并且形参最后要加个BlockException参数
    //否则会报错，FlowException: null

    public User queryUserByUserNameBlock(String userName, BlockException ex) {
        //打印异常
        ex.printStackTrace();
        return new User("xxx", "用户名称：{" + userName + "},资源访问被限流", 0);
    }
    public User queryUserByUserNameFallBack(String userName, Throwable ex) {
        //打印日志
        ex.printStackTrace();
        return new User("-1", "用户名称：{" + userName + "},系统异常，请稍后重试", 0);
    }

    @Override
    @SentinelResource(value = "getByAge", blockHandler = "ageBlock")
    public User queryByAge(int age) {
        return new User("0", "柳儿", age);
    }

    public User ageBlock(int age, BlockException ex) {
        //打印异常
        ex.printStackTrace();
        return new User("xxx", "用户年龄：{" + age + "},资源访问被限流", 0);
    }

}
