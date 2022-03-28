package com.sentinel.demo.service;

import com.sentinel.demo.entity.User;

import java.util.List;

/**
 * @author Brave
 * @version V1.0
 * @date 2021/8/27
 */
public interface DemoService {

    List<User> getList();

    User queryByUserName(String userName);

    User queryByAge(int age);

}
