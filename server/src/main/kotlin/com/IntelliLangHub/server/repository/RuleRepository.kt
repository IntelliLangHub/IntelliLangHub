package com.intellilanghub.server.repository

import com.intellilanghub.server.model.Rule
import org.springframework.data.mongodb.repository.MongoRepository


interface RuleRepository : MongoRepository<Rule, String>
