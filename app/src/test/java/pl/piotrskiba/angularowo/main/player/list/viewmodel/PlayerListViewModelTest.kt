package pl.piotrskiba.angularowo.main.player.list.viewmodel

import android.view.View
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.base.rx.TestSchedulersFacade
import pl.piotrskiba.angularowo.main.player.list.nav.PlayerListNavigator
import pl.piotrskiba.angularowo.main.player.model.PlayerBanner

class PlayerListViewModelTest {

    val navigator: PlayerListNavigator = mockk(relaxed = true)
    val tested = PlayerListViewModel(
        observeOnlinePlayerListWithFavoriteInformationUseCase = mockk(),
        refreshOnlinePlayerListUseCase = mockk(),
        facade = TestSchedulersFacade(),
    )

    @Test
    fun `SHOULD navigate to player details WHEN onPlayerClick called`() {
        val view: View = mockk()
        val playerBanner: PlayerBanner = mockk()
        tested.navigator = navigator

        tested.onPlayerClick(view, playerBanner)

        verify { tested.navigator.navigateToPlayerDetails(view, playerBanner) }
    }
}
