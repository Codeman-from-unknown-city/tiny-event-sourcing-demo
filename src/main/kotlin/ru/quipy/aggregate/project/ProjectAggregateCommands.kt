package ru.quipy.aggregate.project

import ru.quipy.api.project.*
import ru.quipy.projections.task.TaskProjection
import ru.quipy.projections.user.UserProjection
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: UUID): ProjectCreatedEvent {
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        creatorId = creatorId,
        defaultStatus = StatusEntity.default(projectId = id)
    )
}

fun ProjectAggregateState.addUser(projectId: UUID, userId: UUID): AddUserToProjectEvent {
    var userAlreadyExist = false;
    this.projectMemberIds.forEach { element ->
        if (element == userId){
            userAlreadyExist = true;
        }
    }

    if (userAlreadyExist){
        throw IllegalArgumentException("User already exist in this project: $userId")
    }

    return AddUserToProjectEvent(projectId = projectId, userId = userId);
}

fun ProjectAggregateState.changeTitle(projectId: UUID, title: String): ProjectTitleChangedEvent {
    return ProjectTitleChangedEvent(projectId = projectId, title = title);
}

fun ProjectAggregateState.addStatus(name: String, color: String): StatusCreatedEvent {
    return StatusCreatedEvent(projectId = this.getId(), statusId = UUID.randomUUID(), statusName = name, color = color)
}

fun ProjectAggregateState.removeStatus(
    statusId: UUID,
    projectId: UUID,
    taskProjection: TaskProjection
): StatusDeletedEvent {
    if (!projectStatus.containsKey(statusId)){
        throw IllegalArgumentException("Status doesn't exists: $statusId")
    }

    val tasks = taskProjection.findAllByStatusId(statusId)
    if (tasks.toList().isNotEmpty())
        throw IllegalArgumentException("Status has tasks")

    return StatusDeletedEvent(statusId, projectId)
}

fun ProjectAggregateState.addTask(name: String): TaskCreatedEvent {
    return TaskCreatedEvent(projectId = this.getId(), taskId = UUID.randomUUID(), title = name)
}

fun ProjectAggregateState.changeTaskTitle(taskId: UUID, title: String): TaskTitleChangedEvent {
    return  TaskTitleChangedEvent(taskId = taskId, title = title)
}

fun ProjectAggregateState.changeTaskStatus(taskId: UUID, statusId: UUID): TaskStatusChangedEvent {
    if (!projectStatus.containsKey(statusId)){
        throw IllegalArgumentException("Status doesn't exists: $statusId")
    }

    return TaskStatusChangedEvent(taskId = taskId, statusId = statusId)
}

fun ProjectAggregateState.memberAssignedToTask(userId: UUID,
                                               taskId: UUID,
                                               taskProjection: TaskProjection,
                                               userProjection: UserProjection): MemberAssignedToTaskEvent{
    if (!taskProjection.findById(taskId).isPresent){
        throw IllegalArgumentException("Task not exist: $taskId")
    }

    if (!userProjection.findById(userId).isPresent){
        throw IllegalArgumentException("User not exist: $userId")
    }

    return MemberAssignedToTaskEvent(taskId = taskId, userId = userId)
}