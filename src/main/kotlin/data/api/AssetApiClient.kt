package genesischallenge.data.api

import genesischallenge.data.model.AssetResponse
import genesischallenge.data.model.CoinCapResponse
import genesischallenge.data.model.PriceResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

const val BASE_URL = "https://api.coincap.io/v2/assets"

class AssetApiClient(private val client: HttpClient = configureHttpClient()) {

    suspend fun fetchAsset(symbol: String): CoinCapResponse<List<AssetResponse>> {
        return try {
            client.get(BASE_URL) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                parameter("search", symbol)
                //using the limit parameter from the API to avoid any unwanted extra information
                parameter("limit", 1)
            }.body<CoinCapResponse<List<AssetResponse>>>()
        } catch (e: Exception) {
            //logging the exception and returning a neutral value so the rest of the process don't break
            println("An exception happened when fetching the Asset for '$symbol': ${e.message}")
            CoinCapResponse(listOf(AssetResponse("")))
        }
    }

    suspend fun fetchPrice(assetId: String): CoinCapResponse<List<PriceResponse>> {
        return try {
            client.get("$BASE_URL/$assetId/history") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                //parameters added according the specifications of the challenge to guarantee the same data
                parameter("interval", "d1")
                parameter("start", "1617753600000")
                parameter("end", "1617753601000")
            }.body<CoinCapResponse<List<PriceResponse>>>()
        } catch (e: Exception) {
            //logging the exception and returning a neutral value so the rest of the process don't break
            println("An exception happened when fetching the Price for '$assetId': ${e.message}")
            CoinCapResponse(listOf(PriceResponse(0.0)))
        }
    }
}
