package pl.piotrskiba.angularowo.main.player.details.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.player.model.PlayerDetailsMenuItemsVisibilityModel

class PlayerDetailsMenuItemsVisibilityTest {

    @Test
    fun `SHOULD map domain model to ui`() {
        val tested = PlayerDetailsMenuItemsVisibilityModel(
            favorite = true,
            unfavorite = false,
            mute = true,
            kick = false,
            warn = true,
            ban = false,
        )

        tested.toUi() shouldBeEqualTo PlayerDetailsMenuItemsVisibility(
            favorite = true,
            unfavorite = false,
            mute = true,
            kick = false,
            warn = true,
            ban = false,
        )
    }
}
