package pl.piotrskiba.angularowo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Friend(@PrimaryKey val uuid: String)