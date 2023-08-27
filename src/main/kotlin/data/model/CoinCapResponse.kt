package genesischallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
open class CoinCapResponse<T>(val data: T)