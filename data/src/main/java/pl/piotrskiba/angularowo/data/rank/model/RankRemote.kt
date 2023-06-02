package pl.piotrskiba.angularowo.data.rank.model

import pl.piotrskiba.angularowo.domain.rank.model.RankModel

data class RankRemote(
    val name: String,
    val staff: Boolean,
    val colorCode: String,
    val chatColorCode: String,
)

fun RankRemote.toDomain() =
    RankModel(
        name = name,
        staff = staff,
        colorCode = colorCode,
        chatColorCode = chatColorCode,
    )

fun List<RankRemote>.toDomain() =
    map { it.toDomain() }
