package pl.piotrskiba.angularowo.data.chat.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.chat.ChatApiService
import pl.piotrskiba.angularowo.data.chat.ChatWebSocket
import pl.piotrskiba.angularowo.data.chat.repository.ChatRepositoryImpl
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.domain.chat.repository.ChatRepository
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class ChatModule {

    @Provides
    fun provideChatApiService(retrofit: Retrofit): ChatApiService =
        retrofit.create(ChatApiService::class.java)

    @Provides
    fun provideChatRepository(chatApiService: ChatApiService, chatWebSocket: ChatWebSocket): ChatRepository =
        ChatRepositoryImpl(chatApiService, chatWebSocket)
}
