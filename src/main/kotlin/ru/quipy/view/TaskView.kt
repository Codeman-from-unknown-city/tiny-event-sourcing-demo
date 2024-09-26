package ru.quipy.view

import ru.quipy.projections.task.TaskProjectionData
import java.util.*

data class TaskView (
    val id: UUID,
    val title: String,
    val assignee: UserView?,
    val status: StatusView?
) {
    companion object {
        fun fromProjection(projection: TaskProjectionData, assignee: UserView?, status: StatusView?) = with(projection) {
            TaskView(id = id, title = title, assignee = assignee, status = status)
        }
    }
}
