package ru.quipy.projections.statusTask

import org.springframework.stereotype.Service;
import ru.quipy.aggregate.project.ProjectAggregate;
import ru.quipy.api.project.StatusCreatedEvent
import ru.quipy.api.project.StatusDeletedEvent
import ru.quipy.api.project.TaskStatusChangedEvent
import ru.quipy.streams.annotation.AggregateSubscriber;
import ru.quipy.streams.annotation.SubscribeEvent;
import java.util.Optional
import java.util.UUID

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class,
    subscriberName = "status-task-sub"
)
class StatusTasksService(
    private val statusTasksRepository: StatusTasksRepository,
) {
    @SubscribeEvent
    fun saveStatus(event: StatusCreatedEvent) {
        statusTasksRepository.save(Status(event.statusId, ArrayList()))
    }

    @SubscribeEvent
    fun addTask(event: TaskStatusChangedEvent) {
        val prevStatus = statusTasksRepository.findStatusByTasksContaining(event.taskId)
        if (prevStatus.isPresent) {
            val prevStatus = prevStatus.get()
            statusTasksRepository.updateById(prevStatus.id, Status(
                prevStatus.id,
                    prevStatus.tasks.filter { tid -> tid != event.taskId } as MutableList<UUID>
            ))
        }
        val newStatus = statusTasksRepository.findById(event.statusId).get()
        newStatus.tasks.add(event.taskId)
        statusTasksRepository.updateById(event.statusId, newStatus)
    }

    @SubscribeEvent
    fun removeEvent(event: StatusDeletedEvent) {
        statusTasksRepository.deleteById(event.statusId)
    }

    fun getStatusTasks(statusId: UUID): Optional<List<UUID>> {
        val status = statusTasksRepository.findById(statusId)
        return if (status.isPresent) Optional.of(status.get().tasks) else Optional.empty()
    }
}