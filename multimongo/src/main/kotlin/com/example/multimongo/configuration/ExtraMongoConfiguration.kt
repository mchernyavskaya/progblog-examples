package com.example.multimongo.configuration

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.util.SocketUtils


@Configuration
class ExtraMongoConfiguration {

    val uri: String? = null
    val host: String? = null
    val port: Int? = 0
    val database: String? = null

    /**
     * Method that creates MongoClient
     */
    private val mongoClient: MongoClient
        get() {
            if (uri != null && !uri.isNullOrEmpty()) {
                return MongoClient(MongoClientURI(uri!!))
            }
            return MongoClient(host!!, port!!)
        }


    /**
     * Factory method to create the MongoTemplate
     */
    protected fun mongoTemplate(): MongoTemplate {
        val factory = SimpleMongoDbFactory(mongoClient, database!!)
        return MongoTemplate(factory)
    }
}


@EnableMongoRepositories(
    basePackages = ["com.example.multimongo.data.external"],
    mongoTemplateRef = "externalMongoTemplate")
@Configuration
class ExternalDatabaseConfiguration : ExtraMongoConfiguration() {
    @Value("\${additional-db.external.uri:}")
    override val uri: String? = null
    @Value("\${additional-db.external.host:}")
    override val host: String? = null
    @Value("\${additional-db.external.port:0}")
    override val port: Int? = 0
    @Value("\${additional-db.external.database:}")
    override val database: String? = null

    @Bean("externalMongoTemplate")
    fun externalMongoTemplate(): MongoTemplate = mongoTemplate()
}

@EnableMongoRepositories(
    basePackages = ["com.example.multimongo.data.internal"],
    mongoTemplateRef = "internalMongoTemplate")
@Configuration
class InternalDatabaseConfiguration : ExtraMongoConfiguration() {
    @Value("\${additional-db.internal.uri:}")
    override val uri: String? = null
    @Value("\${additional-db.internal.host:}")
    override val host: String? = null
    @Value("\${additional-db.internal.port:0}")
    override val port: Int? = 0
    @Value("\${additional-db.internal.database:}")
    override val database: String? = null

    @Bean("internalMongoTemplate")
    fun internalMongoTemplate(): MongoTemplate = mongoTemplate()
}
