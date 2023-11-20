package ru.quipy.projections.userProject

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.aggregate.project.StatusEntity
import ru.quipy.aggregate.project.TaskEntity
import java.util.*

@Document("user-project")
data class UserProject(
    @Id
    val userId: UUID,
    val projects: MutableList<UUID>
)