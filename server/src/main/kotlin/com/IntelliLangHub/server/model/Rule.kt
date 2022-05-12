package com.intellilanghub.server.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Rule(
    val injection: String,
    val library: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    lateinit var id: String
}