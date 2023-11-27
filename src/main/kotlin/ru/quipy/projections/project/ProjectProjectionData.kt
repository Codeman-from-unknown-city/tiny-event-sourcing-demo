package ru.quipy.projections.project

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("project-projection")
data class ProjectProjectionData (
    @Id
    var id: UUID,
    var title: String,
    val projectMemberIds: MutableList<UUID>,
    val tasks: MutableList<UUID>,
    val creatorId: UUID,
    val projectStatus: MutableList<UUID>,
    val defaultStatus: UUID
)