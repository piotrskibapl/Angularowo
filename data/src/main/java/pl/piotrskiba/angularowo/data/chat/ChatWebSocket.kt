package pl.piotrskiba.angularowo.data.chat

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import pl.piotrskiba.angularowo.data.chat.model.ChatMessageRemote
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import javax.inject.Inject

private const val CHAT_WEBSOCKET_URL = "ws://asmc-serwer.piotrskiba.pl:25772"
private const val ACCESS_TOKEN_HEADER = "access_token"
private const val CLOSE_CODE_NORMAL = 1000
private const val CLOSE_REASON_DISCONNECTED = "Disconnected"

class ChatWebSocket @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val preferencesRepository: PreferencesRepository,
) {

    private var webSocket: WebSocket? = null
    private val emitters = mutableListOf<ObservableEmitter<ChatMessageRemote>>()

    fun open(): Observable<ChatMessageRemote> {
        if (webSocket == null) {
            webSocket = okHttpClient.newWebSocket(
                request = Request.Builder()
                    .url(CHAT_WEBSOCKET_URL)
                    .addHeader(ACCESS_TOKEN_HEADER, preferencesRepository.accessToken().blockingGet()!!)
                    .build(),
                listener = object : WebSocketListener() {

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        val data = text.split(";")
                        val uuid = data[0]
                        val username = data[1]
                        val rankName = data[2]
                        val message = data[3]
                        emitNext(ChatMessageRemote(uuid, username, rankName, message))
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        this@ChatWebSocket.webSocket = null
                        emitComplete()
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        this@ChatWebSocket.webSocket = null
                        emitError(t)
                    }
                },
            )
        }
        return Observable.create { emitter ->
            emitters.add(emitter)
            emitter.setDisposable(
                Disposable.fromRunnable {
                    emitters.removeAll { it.isDisposed }
                    if (emitters.isEmpty()) {
                        webSocket?.close(CLOSE_CODE_NORMAL, CLOSE_REASON_DISCONNECTED)
                        webSocket = null
                    }
                },
            )
        }
    }

    private fun emitNext(message: ChatMessageRemote) {
        emitters.forEach {
            it.onNext(message)
        }
    }

    private fun emitComplete() {
        emitters.forEach {
            it.onComplete()
        }
    }

    private fun emitError(error: Throwable) {
        emitters.forEach {
            it.onError(error)
        }
    }
}
