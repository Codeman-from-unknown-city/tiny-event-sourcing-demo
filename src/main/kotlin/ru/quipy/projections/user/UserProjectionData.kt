package ru.quipy.projections.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("users-projections")
data class UserProjectionData (
    @Id
    val id: UUID,
    var name: String,
    val nickname: String,
)