package com.openai.mydataagent.adapter.out.vectorDB

import com.openai.mydataagent.application.port.out.RagPort
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.WeaviateVectorStore
import org.springframework.stereotype.Repository

@Repository
class WeaviateImpl(private val vectorStore: WeaviateVectorStore) : RagPort{
    override fun findRagByWord(query: String): List<Document> {
        return vectorStore.similaritySearch(SearchRequest.query(query).withTopK(3))
    }

    override fun savaRagDocument(content: List<String>) {
        try {
            val documents = content.map {
                Document.builder()
                    .withContent(it)
                    .build()
            }

            vectorStore.add(documents)
        } catch (e: Exception){
            throw e
        }
    }
}
