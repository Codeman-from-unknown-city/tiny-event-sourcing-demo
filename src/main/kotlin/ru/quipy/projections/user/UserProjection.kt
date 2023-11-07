package ru.quipy.projections.user

import org.springframework.stereotype.Service
import ru.quipy.aggregate.user.UserAggregate
import ru.quipy.api.user.UserChangedNameEvent
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import java.util.*

@Service
@AggregateSubscriber(
    aggregateClass = UserAggregate::class, subscriberName = "user-data-sub"
)
class UserProjection(
    private val userRepository: UsersRepository
) {
    @SubscribeEvent
    fun saveUser(event: UserCreatedEvent) {
        userRepository.save(
            User(event.userId, event.firstname, event.nickname)
        )
    }

    @SubscribeEvent
    fun updateNameUser(event: UserChangedNameEvent) {
        userRepository.updateUserNameById(event.userId, event.newName)
    }

    fun findByName(name: String): Optional<User> {
        return userRepository.findByName(name)
    }

    fun findById(id: UUID): Optional<User> {
        return userRepository.findById(id)
    }

    fun findByNameSubs(nameSubs: String): List<User> {
        return userRepository.findAllByNameRegex(nameSubs)
    }

    fun findByNickNameSubs(nicknameSubs: String): List<User> {
        return userRepository.findAllByNicknameRegex(nicknameSubs)
    }

    fun findByNickName(nickname: String): Optional<User> {
        return userRepository.findByNickname(nickname)
    }
}