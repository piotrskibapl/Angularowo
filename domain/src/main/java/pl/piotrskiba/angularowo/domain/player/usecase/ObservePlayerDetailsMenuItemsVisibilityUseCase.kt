package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import pl.piotrskiba.angularowo.domain.player.model.PermissionModel
import pl.piotrskiba.angularowo.domain.player.model.PlayerDetailsMenuItemsVisibilityModel
import javax.inject.Inject

class ObservePlayerDetailsMenuItemsVisibilityUseCase @Inject constructor(
    private val getAppUserPlayerUseCase: GetAppUserPlayerUseCase,
    private val friendRepository: FriendRepository,
) {

    fun execute(previewedPlayerUuid: String): Observable<PlayerDetailsMenuItemsVisibilityModel> =
        getAppUserPlayerUseCase.execute(ignoreCache = false)
            .flatMapObservable { appUserPlayer ->
                friendRepository.getAllFriends()
                    .map { friends -> friends.any { it.uuid == previewedPlayerUuid } }
                    .map { isPreviewedPlayerFavorite ->
                        val isPreviewingSelfOrPartner =
                            previewedPlayerUuid == appUserPlayer.uuid || previewedPlayerUuid == appUserPlayer.partnerUuid
                        PlayerDetailsMenuItemsVisibilityModel(
                            favorite = !isPreviewingSelfOrPartner && !isPreviewedPlayerFavorite,
                            unfavorite = !isPreviewingSelfOrPartner && isPreviewedPlayerFavorite,
                            mute = appUserPlayer.permissions.contains(PermissionModel.MUTE_PLAYERS),
                            kick = appUserPlayer.permissions.contains(PermissionModel.KICK_PLAYERS),
                            warn = appUserPlayer.permissions.contains(PermissionModel.WARN_PLAYERS),
                            ban = appUserPlayer.permissions.contains(PermissionModel.BAN_PLAYERS),
                        )
                    }
            }
}
