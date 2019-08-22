package com.example.multimongo.configuration

import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.example.multimongo.data.core"])
@Import(value = [MongoAutoConfiguration::class])
class CoreMongoConfiguration {
    @Bean
    fun mongoTemplate(mongoDbFactory: MongoDbFactory): MongoTemplate {
        return MongoTemplate(mongoDbFactory)
    }
}
