package ru.quipy.projections.task

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("task-projection")
data class TaskProjectionData (
    @Id
    var id: UUID,
    var title: String,
    var assigneeId: UUID? = null,
    val statusId: UUID? = null
)