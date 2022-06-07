package com.intellilanghub.server.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager

@Configuration
class MongoConfiguration {
    @Bean
    fun txManager(mongoDbFactory: MongoDatabaseFactory): MongoTransactionManager =
        MongoTransactionManager(mongoDbFactory)
}