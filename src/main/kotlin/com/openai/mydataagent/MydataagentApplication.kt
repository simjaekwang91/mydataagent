package com.openai.mydataagent

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MydataagentApplication

fun main(args: Array<String>) {
    runApplication<MydataagentApplication>(*args)
}
