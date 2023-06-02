package pl.piotrskiba.angularowo.domain.rank.model

import java.io.Serializable

data class RankModel(
    val name: String,
    val staff: Boolean,
    val colorCode: String,
    val chatColorCode: String,
) : Serializable {

    companion object {

        fun unknownRank(name: String) =
            RankModel(name, staff = false, colorCode = "7", chatColorCode = "7")
    }
}
