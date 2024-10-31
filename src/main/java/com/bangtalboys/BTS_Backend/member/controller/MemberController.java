package com.bangtalboys.BTS_Backend.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class MemberController {

    @ResponseBody // View 페이지가 아닌 응답값 그대로 반환하기 위해 사용
    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public String helloWorld() {
        return "hello world";
    }
}