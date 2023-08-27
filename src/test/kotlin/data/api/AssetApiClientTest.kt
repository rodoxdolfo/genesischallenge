import genesischallenge.data.api.AssetApiClient
import genesischallenge.data.api.BASE_URL
import genesischallenge.utils.roundTo2Decimal
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class AssetApiClientTest {
    private val client = HttpClient(MockEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        engine {
            addHandler { request ->
                when (request.url.toString()) {
                    "$BASE_URL?search=BTC&limit=1" -> {
                        val responseHeaders =
                            headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                        respond(
                            content = """
                                {
                                    "data": [
                                        {
                                            "id": "bitcoin",
                                            "rank": "1",
                                            "symbol": "BTC",
                                            "name": "Bitcoin",
                                            "supply": "18626237.0000000000000000",
                                            "maxSupply": "21000000.0000000000000000",
                                            "marketCapUsd": "1070842170678.5321044360848200",
                                            "volumeUsd24Hr": "13249495605.8167918159936900",
                                            "priceUsd": "57471.3758021417915274",
                                            "changePercent24Hr": "-1.1236280380296600",
                                            "vwap24Hr": "57636.7595682016980056"
                                        }
                                    ],
                                    "timestamp": 1617753601000
                                }
                            """.trimIndent(),
                            headers = responseHeaders
                        )
                    }

                    "$BASE_URL?search=XXX&limit=1" -> {
                        throw ConnectTimeoutException(request)
                    }

                    "$BASE_URL/bitcoin/history?interval=d1&start=1617753600000&end=1617753601000" -> {
                        val responseHeaders =
                            headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                        respond(
                            content = """
                                {
                                    "data": [
                                        {
                                            "priceUsd": "57471.3758021417915274",
                                            "time": 1617753600000,
                                            "date": "2021-04-06T22:00:00.000Z"
                                        },
                                        {
                                            "priceUsd": "57471.3758021417915274",
                                            "time": 1617753601000,
                                            "date": "2021-04-06T22:00:01.000Z"
                                        }
                                    ],
                                    "timestamp": 1617753601000
                                }
                            """.trimIndent(),
                            headers = responseHeaders
                        )
                    }

                    "$BASE_URL/X/history?interval=d1&start=1617753600000&end=1617753601000" -> {
                        throw ConnectTimeoutException(request)
                    }

                    else -> error("Unhandled ${request.url}")
                }
            }
        }
    }

    private val apiClient = AssetApiClient(client)

    @Test
    fun `test fetchAsset success`() = runTest {
        val assetResponse = apiClient.fetchAsset("BTC")
        assertEquals("bitcoin", assetResponse.data.first().id)
    }

    @Test
    fun `test fetchAsset error`() = runTest {
        val assetResponse = apiClient.fetchAsset("XXX")
        assertEquals("", assetResponse.data.first().id)
    }

    @Test
    fun `test fetchPrice success`() = runTest {
        val historyResponse = apiClient.fetchPrice("bitcoin")
        assertEquals(2, historyResponse.data.size)
        assertEquals("57471.38", historyResponse.data.first().priceUsd.roundTo2Decimal())
        assertEquals("57471.38", historyResponse.data.last().priceUsd.roundTo2Decimal())
    }

    @Test
    fun `test fetchPrice error`() = runTest {
        val historyResponse = apiClient.fetchPrice("X")
        assertEquals(1, historyResponse.data.size)
        assertEquals("0.00", historyResponse.data.first().priceUsd.roundTo2Decimal())
    }
}
