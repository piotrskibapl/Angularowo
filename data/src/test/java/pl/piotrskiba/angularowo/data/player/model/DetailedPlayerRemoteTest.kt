package pl.piotrskiba.angularowo.data.player.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PermissionModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

class DetailedPlayerRemoteTest {

    companion object {

        @JvmStatic
        fun parameters() = listOf(
            listOf("ignore_app_lock") to listOf(PermissionModel.IGNORE_APP_LOCK),
            listOf("kick_players") to listOf(PermissionModel.KICK_PLAYERS),
            listOf("mute_players") to listOf(PermissionModel.MUTE_PLAYERS),
            listOf("warn_players") to listOf(PermissionModel.WARN_PLAYERS),
            listOf("bAn_PlAyErS") to listOf(PermissionModel.BAN_PLAYERS),
            listOf("unknown_permission") to listOf(),
            listOf("ignore_app_lock", "unknown_permission", "kick_players") to listOf(PermissionModel.IGNORE_APP_LOCK, PermissionModel.KICK_PLAYERS),
        ).map { Arguments.of(it.first, it.second) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD map remote object to domain`(permissionsRemote: List<String>, permissionModels: List<PermissionModel>) {
        val tested = DetailedPlayerRemote(
            uuid = "uuid",
            skinUuid = "skinUuid",
            partnerUuid = "partnerUuid",
            username = "username",
            status = "status",
            rank = "rank",
            vanished = false,
            balance = 123f,
            playtime = 456L,
            tokens = 789,
            permissions = permissionsRemote,
        )

        tested.toDomain() shouldBeEqualTo DetailedPlayerModel(
            uuid = "uuid",
            skinUuid = "skinUuid",
            partnerUuid = "partnerUuid",
            username = "username",
            status = "status",
            rank = RankModel.unknownRank("rank"),
            isVanished = false,
            balance = 123f,
            playtime = 456L,
            tokens = 789,
            permissions = permissionModels,
        )
    }
}
