package com.intellilanghub.server.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class InjectionPack(
    var injectionConfiguration: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @Id
    val library: String,
)