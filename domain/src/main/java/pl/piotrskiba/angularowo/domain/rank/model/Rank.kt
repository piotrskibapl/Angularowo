package pl.piotrskiba.angularowo.domain.rank.model

sealed class Rank(val name: String) {
    data class UnknownRank(private val rankName: String) : Rank(rankName)
    data class CustomRank(
        private val rankName: String,
        val staff: Boolean,
        val colorCode: String,
        val chatColorCode: String
    ) : Rank(rankName)
}

fun String.toNewRank() = Rank.CustomRank(
    this,
    false,
    "7", // TODO: get default color codes from strings.yml
    "7"
)
