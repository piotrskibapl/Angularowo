package pl.piotrskiba.angularowo.data.server.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.server.model.MotdModel

class MotdRemoteTest {

    @Test
    fun `SHOULD map remote object to domain`() {
        val tested = MotdRemote(
            text = "text",
            url = "http://google.com/",
            textColor = "#fff",
            backgroundColor = "#000",
        )

        tested.toDomain() shouldBeEqualTo MotdModel(
            text = "text",
            url = "http://google.com/",
            textColor = "#fff",
            backgroundColor = "#000",
        )
    }
}
