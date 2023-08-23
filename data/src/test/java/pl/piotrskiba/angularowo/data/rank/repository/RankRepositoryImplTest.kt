package pl.piotrskiba.angularowo.data.rank.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.data.rank.model.RankRemote
import pl.piotrskiba.angularowo.data.rank.model.toDomain
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

class RankRepositoryImplTest {

    val firebaseRemoteConfig: FirebaseRemoteConfig = mockk()
    val gson: Gson = mockk()
    val tested = RankRepositoryImpl(firebaseRemoteConfig, gson)

    @BeforeEach
    fun setup() {
        mockkStatic(List<RankRemote>::toDomain)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD get all ranks`() {
        val ranksJson = "json"
        val ranks: List<RankModel> = mockk()
        val ranksRemote: List<RankRemote> = mockk {
            every { toDomain() } returns ranks
        }
        every { firebaseRemoteConfig.getString("ranks") } returns ranksJson
        every { gson.fromJson<List<RankRemote>>(ranksJson, object : TypeToken<List<RankRemote?>?>() {}.type) } returns ranksRemote

        val result = tested.getAllRanks().test()

        result.assertValue(ranks)
    }
}
