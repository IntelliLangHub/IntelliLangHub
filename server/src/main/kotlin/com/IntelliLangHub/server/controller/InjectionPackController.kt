package com.intellilanghub.server.controller

import com.intellilanghub.server.model.InjectionPack
import com.intellilanghub.server.service.InjectionPackService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/injection-pack")
@RestController
class InjectionPackController(private val injectionPackService: InjectionPackService) {
    @GetMapping("")
    fun getInjectionPackLibraries(): List<String> {
        return injectionPackService.getInjectionPackLibraries()
    }

    @GetMapping("{library}")
    fun getInjectionPack(@PathVariable library: String): InjectionPack {
        return injectionPackService.getInjectionPack(library)
    }
}