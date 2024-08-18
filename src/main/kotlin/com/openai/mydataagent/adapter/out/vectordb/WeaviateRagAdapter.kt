package com.openai.mydataagent.adapter.out.vectordb

import com.openai.mydataagent.application.port.out.RagPort
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.WeaviateVectorStore
import org.springframework.stereotype.Repository

/**
 * Weaviate rag adapter
 * PDF VectorDB 저장
 *
 * @property vectorStore
 * @constructor Create empty Weaviate rag adapter
 */
@Repository
class WeaviateRagAdapter(private val vectorStore: WeaviateVectorStore) : RagPort {
    override fun searchSimilarVectors(query: String): List<String> {
        val threshold = 0.7  // 임계값 설정
        return vectorStore.similaritySearch(
            SearchRequest.query(query)
                .withSimilarityThreshold(threshold).withTopK(6)
        ).map {
            it.content
        }
    }

    override fun savaRagDocument(content: List<String>) {
        try {
            val documents = content.map {
                Document.builder()
                    .withContent(it)
                    .build()
            }

            vectorStore.add(documents)
        } catch (e: Exception) {
            throw e
        }
    }
}
