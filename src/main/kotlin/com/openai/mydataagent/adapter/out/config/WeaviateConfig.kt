package com.openai.mydataagent.adapter.out.config

import io.weaviate.client.Config
import io.weaviate.client.WeaviateAuthClient
import io.weaviate.client.WeaviateClient
import io.weaviate.client.v1.auth.exception.AuthException
import org.springframework.ai.embedding.Embedding
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.WeaviateVectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WeaviateConfig {
    @Bean
    fun weaviateClient(): WeaviateClient {
        try {
            return WeaviateAuthClient.apiKey(
                Config("https", "izfbxjnisyzzpqnai62w.c0.asia-southeast1.gcp.weaviate.cloud", mapOf("Content-Type" to "application/json")),
            "AVm6AIvYXFDUJMQYK8pLRsSC4sQ0hEqlxCv0")
        }
        catch (e: AuthException) {
            throw IllegalArgumentException("WeaviateClient could not be created.", e)
        }
    }

//    @Bean
//    fun vectorStore(embeddingModel: EmbeddingModel): WeaviateVectorStore {
//
//        val config = WeaviateVectorStore.WeaviateVectorStoreConfig.builder()
//            .withObjectClass("Test")
//            .withConsistencyLevel(WeaviateVectorStore.WeaviateVectorStoreConfig.ConsistentLevel.ONE) // 예시: ConsistencyLevel을 "ONE"으로 설정
//            .build()
//
//        return WeaviateVectorStore(config, embeddingModel)
//    }

}
