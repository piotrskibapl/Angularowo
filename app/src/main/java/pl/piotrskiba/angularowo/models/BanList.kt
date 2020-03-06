package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName

class BanList(
        @field:SerializedName("banlist")
        val banList: List<Ban>)