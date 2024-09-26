package ru.quipy.projections.user

import org.springframework.stereotype.Service
import ru.quipy.aggregate.user.UserAggregate
import ru.quipy.api.user.UserChangedNameEvent
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = UserAggregate::class, subscriberName = "user-projection-service"
)
class UserProjectionService(
    private val userProjection: UserProjection
) {
    @SubscribeEvent
    fun saveUser(event: UserCreatedEvent) = userProjection.save(UserProjectionData(event.userId, event.firstname, event.nickname))

    @SubscribeEvent
    fun updateNameUser(event: UserChangedNameEvent) = userProjection.updateUserNameById(event.userId, event.newName)
}