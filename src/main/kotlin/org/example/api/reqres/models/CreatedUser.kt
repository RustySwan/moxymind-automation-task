package org.example.api.reqres.models

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.ALWAYS)
data class CreatedUser(
    val name: String?,
    val job: String?,
    val id: String?,
    val createdAt: String?,
)

