package org.example.api.reqres.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.ALWAYS)
data class Users(
    val page: Int,
    @JsonProperty("per_page")
    val perPage: Int,
    val total: Int,
    @JsonProperty("total_pages")
    val totalPages: Int,
    val data: List<Data>,
    val support: Support,
)

data class Data(
    val id: Int,
    val email: String,
    @JsonProperty("first_name")
    val firstName: String,
    @JsonProperty("last_name")
    val lastName: String,
    val avatar: String,
)

data class Support(
    val url: String,
    val text: String,
)
