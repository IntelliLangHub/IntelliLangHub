package com.intellilanghub.server.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

enum class CommitStatus {
    ACTIVE, ACCEPTED, REJECTED
}

@Document
data class Commit(
    val injections: List<String>,
    val library: String,
    var status: CommitStatus = CommitStatus.ACTIVE,
    val createdDate: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    lateinit var id: String
}