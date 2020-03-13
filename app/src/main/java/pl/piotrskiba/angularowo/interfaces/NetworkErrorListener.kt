package pl.piotrskiba.angularowo.interfaces

interface NetworkErrorListener {
    fun onNoInternet()
    fun onServerError()
}