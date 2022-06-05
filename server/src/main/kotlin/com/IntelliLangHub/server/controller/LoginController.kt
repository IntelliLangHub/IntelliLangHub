package com.intellilanghub.server.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@RequestMapping("/login")
@Controller
class LoginController() {
    @GetMapping("")
    fun login(): String {
        return "login"
    }
}