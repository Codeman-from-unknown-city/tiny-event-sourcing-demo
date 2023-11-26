package ru.quipy.view

import ru.quipy.projections.status.ProjectProjectionData
import java.util.UUID

data class ProjectView (
    val id: UUID,
    val title: String,
    val projectMemberIds: MutableList<UUID>,
    val tasks: MutableList<UUID>,
    val creatorId: UUID,
    val projectStatus: MutableList<UUID>,
    val defaultStatus: UUID
) {
    companion object {
        fun fromProjection(projection: ProjectProjectionData) = with(projection) {
            ProjectView(id, title, projectMemberIds, tasks, creatorId, projectStatus, defaultStatus)
        }
    }
}