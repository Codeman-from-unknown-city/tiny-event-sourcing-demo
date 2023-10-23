package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: UUID): ProjectCreatedEvent {
    return ProjectCreatedEvent(projectId = id, title = title, creatorId = creatorId)
}

fun ProjectAggregateState.addUser(projectId: UUID, userId: UUID): AddUserToProjectEvent {
    return AddUserToProjectEvent(projectId = projectId, userId = userId);
}

fun ProjectAggregateState.changeTitle(projectId: UUID, title: String): ProjectTitleChangedEvent {
    return ProjectTitleChangedEvent(projectId = projectId, title = title);
}

fun ProjectAggregateState.addStatus(name: String): StatusCreatedEvent {
    return StatusCreatedEvent(projectId = this.getId(), statusId = UUID.randomUUID(), statusName = name, color = null)
}

fun ProjectAggregateState.removeStatus(statusId: UUID, projectId: UUID): StatusDeletedEvent {
    if (!projectStatus.containsKey(statusId)){
        throw IllegalArgumentException("Status doesn't exists: $statusId")
    }

    return StatusDeletedEvent(statusId, projectId)
}

fun ProjectAggregateState.addTask(name: String): TaskCreatedEvent {
    return TaskCreatedEvent(projectId = this.getId(), taskId = UUID.randomUUID(), title = name)
}

fun ProjectAggregateState.changeTaskTitle(taskId: UUID, title: String): TaskTitleChangedEvent{
    return  TaskTitleChangedEvent(taskId = taskId, title = title)
}

fun ProjectAggregateState.changeTaskStatus(taskId: UUID, statusId: UUID): TaskStatusChangedEvent{
    if (!projectStatus.containsKey(statusId)){
        throw IllegalArgumentException("Status doesn't exists: $statusId")
    }

    return TaskStatusChangedEvent(taskId = taskId, statusId = statusId)
}