package ru.quipy.projections.task

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.aggregate.project.ProjectAggregate
import ru.quipy.api.project.MemberAssignedToTaskEvent
import ru.quipy.api.project.TaskCreatedEvent
import ru.quipy.api.project.TaskStatusChangedEvent
import ru.quipy.api.project.TaskTitleChangedEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(ProjectAggregate::class, subscriberName = "task-projection-service")
class TaskProjectionService {
    @Autowired
    private lateinit var taskProjection: TaskProjection

    @SubscribeEvent
    fun addTask(event: TaskCreatedEvent) = with(event) {
        taskProjection.insert(TaskProjectionData(id = taskId, event.title))
    }

    @SubscribeEvent
    fun changeTitle(event: TaskTitleChangedEvent) = with(event) {
        val task = taskProjection.findById(taskId).get()
        task.title = title
        taskProjection.save(task)
    }

    @SubscribeEvent
    fun assignMember(event: MemberAssignedToTaskEvent) = with(event) {
        val task = taskProjection.findById(taskId).get()
        task.assigneeId = userId
        taskProjection.save(task)
    }

    @SubscribeEvent
    fun assignStatus(event: TaskStatusChangedEvent) {

    }
}