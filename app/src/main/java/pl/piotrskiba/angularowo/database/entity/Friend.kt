package pl.piotrskiba.angularowo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class Friend(@PrimaryKey val uuid: String)