package com.openai.mydataagent.application.port.out

interface CacheConversationPort {
    fun <T> setCacheData(key: String, data: T)
    fun <T> getCacheData(key: String, clazz: Class<T>): T?
}
