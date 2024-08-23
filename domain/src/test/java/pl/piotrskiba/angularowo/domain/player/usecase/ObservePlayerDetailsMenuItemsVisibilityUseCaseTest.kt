package pl.piotrskiba.angularowo.domain.player.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PermissionModel
import pl.piotrskiba.angularowo.domain.player.model.PlayerDetailsMenuItemsVisibilityModel

class ObservePlayerDetailsMenuItemsVisibilityUseCaseTest {

    val getAppUserPlayerUseCase: GetAppUserPlayerUseCase = mockk()
    val friendRepository: FriendRepository = mockk()
    val tested = ObservePlayerDetailsMenuItemsVisibilityUseCase(getAppUserPlayerUseCase, friendRepository)

    companion object {

        @JvmStatic
        fun parameters() = listOf(
            TestParameters(
                playerUuid = "uuid", playerPartnerUuid = "partnerUuid", playerPermissions = emptyList(), friendUuid = "friendUuid", previewedPlayerUuid = "randomUuid",
                expectedResult = PlayerDetailsMenuItemsVisibilityModel(
                    favorite = true,
                    unfavorite = false,
                    mute = false,
                    warn = false,
                    kick = false,
                    ban = false,
                ),
            ),
            TestParameters(
                playerUuid = "uuid", playerPartnerUuid = "partnerUuid", playerPermissions = emptyList(), friendUuid = "friendUuid", previewedPlayerUuid = "friendUuid",
                expectedResult = PlayerDetailsMenuItemsVisibilityModel(
                    favorite = false,
                    unfavorite = true,
                    mute = false,
                    warn = false,
                    kick = false,
                    ban = false,
                ),
            ),
            TestParameters(
                playerUuid = "uuid", playerPartnerUuid = "partnerUuid", playerPermissions = emptyList(), friendUuid = "friendUuid", previewedPlayerUuid = "uuid",
                expectedResult = PlayerDetailsMenuItemsVisibilityModel(
                    favorite = false,
                    unfavorite = false,
                    mute = false,
                    warn = false,
                    kick = false,
                    ban = false,
                ),
            ),
            TestParameters(
                playerUuid = "uuid",
                playerPartnerUuid = "partnerUuid",
                playerPermissions = listOf(PermissionModel.KICK_PLAYERS, PermissionModel.BAN_PLAYERS, PermissionModel.WARN_PLAYERS, PermissionModel.MUTE_PLAYERS),
                friendUuid = "friendUuid",
                previewedPlayerUuid = "partnerUuid",
                expectedResult = PlayerDetailsMenuItemsVisibilityModel(
                    favorite = false,
                    unfavorite = true,
                    mute = true,
                    warn = true,
                    kick = true,
                    ban = true,
                ),
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD return proper PlayerDetailsMenuItemsVisibilityModel`(params: TestParameters) {
        val appUserPlayer: DetailedPlayerModel = mockk {
            every { uuid } returns params.playerUuid
            every { partnerUuid } returns params.playerPartnerUuid
            every { permissions } returns params.playerPermissions
        }
        val friendModel: FriendModel = mockk {
            every { uuid } returns params.friendUuid
        }
        every { getAppUserPlayerUseCase.execute(ignoreCache = false) } returns Single.just(appUserPlayer)
        every { friendRepository.getAllFriends() } returns Observable.just(listOf(friendModel))
    }

    data class TestParameters(
        val playerUuid: String,
        val playerPartnerUuid: String,
        val playerPermissions: List<PermissionModel>,
        val friendUuid: String,
        val previewedPlayerUuid: String,
        val expectedResult: PlayerDetailsMenuItemsVisibilityModel,
    )
}
