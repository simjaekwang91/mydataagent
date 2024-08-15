package com.openai.mydataagent.adapter.out.config

import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.transitions.Mongod
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess
import de.flapdoodle.reverse.TransitionWalker
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@Configuration
class MongoConfig(private val mongoProperties: MongoProperties) {
    @Bean
    fun mongoTemplate(mongoDatabaseFactory: MongoDatabaseFactory, mappingMongoConverter: MappingMongoConverter): MongoTemplate {
        return MongoTemplate(mongoDatabaseFactory, mappingMongoConverter)
    }

    @Bean
    fun mongoDatabaseFactory(): SimpleMongoClientDatabaseFactory {
        // Mongod 인스턴스를 초기화하고 설정
        val mongod = Mongod.builder().build()

        // MongoDB의 버전과 시작 프로세스를 설정
        val version = Version.V4_4_11
        val reachedState: TransitionWalker.ReachedState<RunningMongodProcess> = mongod.start(version)

        // MongoDB URI를 얻고, 이를 사용해 MongoDatabaseFactory를 반환
        val mongoUri = "mongodb://localhost:${reachedState.current().serverAddress.port}/mydatabase"
        return SimpleMongoClientDatabaseFactory(mongoUri)
    }

}
