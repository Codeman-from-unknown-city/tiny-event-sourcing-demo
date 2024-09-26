package ru.quipy.view

import ru.quipy.projections.status.StatusProjectionData
import ru.quipy.projections.task.TaskProjectionData
import java.util.Optional
import java.util.UUID

data class StatusView (
    val id: UUID,
    val name: String,
) {
    companion object {
        fun fromProjection(projection: StatusProjectionData) = with(projection) {
            StatusView(id, name)
        }

        fun fromProjection(projection: Optional<StatusProjectionData>) =
            if (projection.isPresent) Optional.of(Companion.fromProjection(projection.get()))
            else Optional.empty()
    }
}