package ru.quipy.view

import ru.quipy.projections.user.UserProjectionData
import java.util.*

data class UserView (
    val id: UUID,
    var name: String,
    val nickname: String,
) {
    companion object {
        fun fromProjection(projection: UserProjectionData) = with(projection) {
            UserView(id = id, name = name, nickname = nickname)
        }

        fun fromProjection(projection: Optional<UserProjectionData>): Optional<UserView> =
            if (projection.isPresent) Optional.of(Companion.fromProjection(projection.get()))
            else Optional.empty<UserView>()

        fun fromProjection(projection: MutableIterable<UserProjectionData>) =
            projection.map { Companion.fromProjection(it) }.toMutableList()
    }
}
