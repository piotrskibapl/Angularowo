package pl.piotrskiba.angularowo.applock.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel
import pl.piotrskiba.angularowo.domain.applock.model.AppLockDataModel

class AppLockDataTest {

    @Test
    fun `SHOULD map domain object to ui`() {
        val tested = AppLockDataModel(
            config = AppLockConfigModel(
                title = "title",
                body = "body",
                startTimestamp = 123L,
                endTimestamp = 456L,
            ),
            canSkip = true,
        )

        tested.toUi() shouldBeEqualTo AppLockData(
            title = "title",
            body = "body",
            canSkip = true,
        )
    }
}
