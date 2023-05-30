package pl.piotrskiba.angularowo.domain.login.model

data class AccessTokenModel(
        val uuid: String,
        val username: String,
        val accessToken: String,
        val message: String,
)
