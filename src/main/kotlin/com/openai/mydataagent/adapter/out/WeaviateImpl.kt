package com.openai.mydataagent.adapter.out

import com.openai.mydataagent.application.port.out.RagPort
import io.weaviate.client.WeaviateClient
import io.weaviate.client.v1.schema.model.Property
import io.weaviate.client.v1.schema.model.WeaviateClass
import org.springframework.stereotype.Repository

@Repository
class WeaviateImpl(private val weaviateClient: WeaviateClient) : RagPort{
    override fun findRagByWord() {
        TODO("Not yet implemented")
    }

    override fun savaRagDocument(className: String, contentName: String, sentences: List<String>) {
        try {
            sentences.forEach { sentence ->
                val weaviateProperties = mapOf(contentName to sentence)

                weaviateClient.data().creator().withClassName(className)
                    .withProperties(weaviateProperties).run()
            }
        } catch (e: Exception){
            throw e
        }
    }
}
