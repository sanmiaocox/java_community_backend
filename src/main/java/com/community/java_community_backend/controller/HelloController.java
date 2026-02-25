package com.community.java_community_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 告诉Spring这是一个控制器，并且方法返回的数据会直接写入HTTP响应体中
public class HelloController {

    @GetMapping("/hello") // 将HTTP GET请求映射到 /hello 路径上
    public String sayHello() {
        return "后端项目启动成功！你好，电影社区！";
    }
}