package pl.piotrskiba.angularowo.domain.rank.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

interface RankRepository {

    fun getAllRanks(): Single<List<RankModel>>
}
