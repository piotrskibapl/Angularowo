package pl.piotrskiba.angularowo.data.friend.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey val uuid: String,
)

fun FriendEntity.toDomain() =
    FriendModel(uuid)

fun List<FriendEntity>.toDomain() =
    map { it.toDomain() }

fun FriendModel.toData() =
    FriendEntity(uuid)
