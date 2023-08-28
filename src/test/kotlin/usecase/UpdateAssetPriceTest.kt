package usecase

import genesischallenge.data.api.AssetApiClient
import genesischallenge.data.api.BASE_URL
import genesischallenge.data.repository.AssetsRepository
import genesischallenge.data.repository.AssetsRepositoryImpl
import genesischallenge.domain.Asset
import genesischallenge.usecase.UpdateAssetPrice
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

class UpdateAssetPriceTest {
    private val DELTA = 1e-15

    companion object {
        @JvmStatic
        @BeforeClass
        fun setup() {
            val client = HttpClient(MockEngine) {
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

                            "$BASE_URL/bitcoin/history?interval=d1&start=1617753600000&end=1617753601000" -> {
                                val responseHeaders =
                                    headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                                respond(
                                    content = """
                                {
                                    "data": [
                                        {
                                            "priceUsd": "56999.9728252053067291",
                                            "time": 1617753600000,
                                            "date": "2021-04-06T22:00:00.000Z"
                                        }
                                    ],
                                    "timestamp": 1617753601000
                                }
                            """.trimIndent(),
                                    headers = responseHeaders
                                )
                            }

                            else -> error("Unhandled ${request.url}")
                        }
                    }
                }
            }
            val apiClient = AssetApiClient(client)
            val performanceModule = module {
                single<AssetsRepository> { AssetsRepositoryImpl(apiClient) }
            }
            GlobalContext.startKoin {
                modules(performanceModule)
            }
        }
    }

    @Test
    fun `test getUpdatedAssetPrice success`() = runTest {
        val updatedAssetsPrices = UpdateAssetPrice().getUpdatedAssetPrice(listOf(Asset(symbol = "BTC")))
        Assert.assertEquals(56999.97282520531, updatedAssetsPrices.first().updatedPrice, DELTA)
    }

    @Test
    fun `test getUpdatedAssetPrice error`() = runTest {
        val updatedAssetsPrices = UpdateAssetPrice().getUpdatedAssetPrice(listOf(Asset(symbol = "XXX")))
        Assert.assertEquals(0.0, updatedAssetsPrices.first().updatedPrice, DELTA)
    }
}
