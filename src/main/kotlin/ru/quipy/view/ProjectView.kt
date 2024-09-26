package ru.quipy.view

import ru.quipy.projections.project.ProjectProjectionData
import ru.quipy.projections.user.UserProjectionData
import java.util.*

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

        fun fromProjection(projection: Optional<ProjectProjectionData>): Optional<ProjectView> =
                if (projection.isPresent) Optional.of(ProjectView.fromProjection(projection.get()))
                else Optional.empty<ProjectView>()

        fun fromProjection(projection: MutableIterable<ProjectProjectionData>) =
                projection.map { ProjectView.fromProjection(it) }.toMutableList()
    }
}