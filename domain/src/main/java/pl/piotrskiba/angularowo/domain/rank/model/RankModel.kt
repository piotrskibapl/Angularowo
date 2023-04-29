package pl.piotrskiba.angularowo.domain.rank.model

import java.io.Serializable

sealed class RankModel(
    val name: String,
    val staff: Boolean,
    val colorCode: String,
    val chatColorCode: String
) : Serializable {
    data class UnknownRank(val _name: String) : RankModel(_name, false, "7", "7")
    data class CustomRank(
        val _name: String,
        val _staff: Boolean,
        val _colorCode: String,
        val _chatColorCode: String
    ) : RankModel(_name, _staff, _colorCode, _chatColorCode)
}
