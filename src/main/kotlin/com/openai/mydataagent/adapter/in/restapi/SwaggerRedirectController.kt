package com.openai.mydataagent.adapter.`in`.restapi

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
class SwaggerRedirectController {
    @GetMapping("/")
    @Operation(hidden = true)
    fun redirectToSwagger(): RedirectView {
        return RedirectView("/swagger-ui.html")
    }
}
