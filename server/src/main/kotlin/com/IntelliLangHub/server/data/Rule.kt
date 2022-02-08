package com.intellilanghub.server.data

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Rule(
    val name: String,
    val rule: String,
    val library: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
) {
    lateinit var id: String
}