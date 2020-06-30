package pl.piotrskiba.angularowo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.piotrskiba.angularowo.models.Player

@Entity
data class Friend(
        @PrimaryKey val uuid: String?,
        val username: String,
        @ColumnInfo(name = "rank_key") val rankKey: String,
        val vanished: Boolean
) {

    fun asPlayer() = Player(uuid, username, rankKey, vanished)

}