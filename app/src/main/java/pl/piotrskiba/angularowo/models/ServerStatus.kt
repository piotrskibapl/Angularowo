package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName

class ServerStatus(
        @field:SerializedName("player_count")
        val playerCount: Int,
        @field:SerializedName("tps_5")
        val tps: Double)