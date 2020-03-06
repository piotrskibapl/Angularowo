package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Ban(
        @field:SerializedName("name")
        val username: String,
        val uuid: String,
        val reason: String,
        val banner: String,
        @field:SerializedName("bantime")
        val banTime: Float,
        @field:SerializedName("expires")
        val expireDate: Float,
        val type: String) : Serializable {

    companion object {
        const val TYPE_BAN = "Ban"
        const val TYPE_MUTE = "Wyciszenie"
        const val TYPE_WARNING = "Ostrzeżenie"
        const val TYPE_KICK = "Wyrzucenie z gry"
    }

}