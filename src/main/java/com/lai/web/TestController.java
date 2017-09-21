package com.lai.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lailai on 2017/9/21.
 */
@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello(@RequestParam String name){
        return "hello "+name;
    }
}
