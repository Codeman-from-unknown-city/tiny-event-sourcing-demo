package ru.quipy.aggregate.user

import ru.quipy.api.user.UserChangedNameEvent
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.projections.user.UserProjection
import java.util.*

class UserAggregateError(message: String?): Error(message)

fun UserAggregateState.create(
    id: UUID,
    name: String,
    nickname: String,
    password: String,
    userProjection: UserProjection
): UserCreatedEvent {
    if (userProjection.findById(id).isPresent)
        throw UserAggregateError("User with `$id` id already exists")
    if (name.isBlank() || nickname.isBlank() || password.isBlank())
        throw UserAggregateError("Name, nickname and password mustn't be blank")
    return UserCreatedEvent(
        userId = id,
        firstname = name,
        nickname = nickname,
        password = password
    )
}

fun UserAggregateState.changeName(newName: String): UserChangedNameEvent {
    if (newName.isBlank())
        throw UserAggregateError("New name mustn't be blank")
    if (newName == name)
        throw UserAggregateError("New name equals to current name")
    return UserChangedNameEvent(
        userId = getId(),
        newName
    )
}