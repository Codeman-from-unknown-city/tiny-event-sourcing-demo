package ru.quipy.projections.status

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.aggregate.project.ProjectAggregate
import ru.quipy.api.project.ProjectCreatedEvent
import ru.quipy.api.project.StatusCreatedEvent
import ru.quipy.api.project.StatusDeletedEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(ProjectAggregate::class, "status-projection-service")
class StatusProjectionService {
    @Autowired
    lateinit var statusProjection: StatusProjection

    @SubscribeEvent
    fun createDefault(event: ProjectCreatedEvent) = with(event) {
        statusProjection.insert(StatusProjectionData(id = defaultStatus.id, name = defaultStatus.name!!))
    }

    @SubscribeEvent
    fun create(event: StatusCreatedEvent) = with(event) {
        statusProjection.insert(StatusProjectionData(id, name))
    }

    @SubscribeEvent
    fun delete(event: StatusDeletedEvent) = with(event) {
        statusProjection.deleteById(id)
    }
}