package ru.quipy.projections.statusTask

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("statusTask-status")
data class Status(
    @Id
    val id: UUID,
    val tasks: MutableList<UUID>,
)