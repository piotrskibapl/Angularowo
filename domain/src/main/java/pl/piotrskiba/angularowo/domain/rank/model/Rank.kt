package pl.piotrskiba.angularowo.domain.rank.model

sealed class Rank(open val name: String) {
    data class UnknownRank(override val name: String) : Rank(name)
    data class CustomRank(
        override val name: String,
        val staff: Boolean,
        val colorCode: String,
        val chatColorCode: String
    ) : Rank(name)
}

fun String.toNewRank() = Rank.CustomRank(
    this,
    false,
    "7", // TODO: get default color codes from strings.yml
    "7"
)
