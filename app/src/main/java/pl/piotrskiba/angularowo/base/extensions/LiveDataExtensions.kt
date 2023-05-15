package pl.piotrskiba.angularowo.base.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.combine

fun <A, B, C> combineLatest(liveData1: LiveData<A>, liveData2: LiveData<B>, transform: (A, B) -> C) =
    liveData1.asFlow()
        .combine(liveData2.asFlow()) { value1, value2 -> transform(value1, value2) }
        .asLiveData()
