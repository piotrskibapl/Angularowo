package pl.piotrskiba.angularowo.domain.rank.model

import java.io.Serializable

sealed class Rank(
    val name: String,
    val staff: Boolean,
    val colorCode: String,
    val chatColorCode: String
) : Serializable {
    data class UnknownRank(val _name: String) : Rank(_name, false, "7", "7")
    data class CustomRank(
        val _name: String,
        val _staff: Boolean,
        val _colorCode: String,
        val _chatColorCode: String
    ) : Rank(_name, _staff, _colorCode, _chatColorCode)
}
