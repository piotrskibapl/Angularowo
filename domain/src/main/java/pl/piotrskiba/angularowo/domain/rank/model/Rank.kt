package pl.piotrskiba.angularowo.domain.rank.model

sealed class Rank {
    data class UnknownRank(val name: String) : Rank()
    data class CustomRank(
        val name: String,
        val staff: Boolean,
        val colorCode: String,
        val chatColorCode: String
    ) : Rank()
}
