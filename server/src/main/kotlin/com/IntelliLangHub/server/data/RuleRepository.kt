package com.intellilanghub.server.data

import org.springframework.data.mongodb.repository.MongoRepository

interface RuleRepository : MongoRepository<Rule, String>