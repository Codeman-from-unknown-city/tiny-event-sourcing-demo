package ru.quipy.projections.status

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("status-projection")
data class StatusProjectionData (
    @Id
    var id: UUID,
    var name: String,
)