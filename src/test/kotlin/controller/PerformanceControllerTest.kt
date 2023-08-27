import genesischallenge.controller.PerformanceController
import genesischallenge.data.api.AssetApiClient
import genesischallenge.data.api.BASE_URL
import genesischallenge.data.repository.AssetsRepository
import genesischallenge.data.repository.AssetsRepositoryImpl
import genesischallenge.data.repository.WalletRepository
import genesischallenge.data.repository.WalletRepositoryImpl
import genesischallenge.domain.Asset
import genesischallenge.domain.WalletPerformance
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import java.io.FileNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PerformanceControllerTest {
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

                            "$BASE_URL?search=ETH&limit=1" -> {
                                val responseHeaders =
                                    headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                                respond(
                                    content = """
                                {
                                    "data": [
                                        {
                                            "id": "ethereum",
                                            "rank": "2",
                                            "symbol": "ETH",
                                            "name": "Ethereum",
                                            "supply": "120131756.1693597400000000",
                                            "maxSupply": null,
                                            "marketCapUsd": "199079254901.0833155188348981",
                                            "volumeUsd24Hr": "658756329.0936747375213544",
                                            "priceUsd": "1657.1742663981762876",
                                            "changePercent24Hr": "0.3372205230169148",
                                            "vwap24Hr": "1654.9555758403717040"
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

                            "$BASE_URL/ethereum/history?interval=d1&start=1617753600000&end=1617753601000" -> {
                                val responseHeaders =
                                    headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                                respond(
                                    content = """
                                {
                                    "data": [
                                        {
                                            "priceUsd": "2032.1394325557042107",
                                            "time": 1617753600000,
                                            "date": "2021-04-07T00:00:00.000Z"
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
                single<WalletRepository> { WalletRepositoryImpl() }
                single<AssetsRepository> { AssetsRepositoryImpl(apiClient) }
            }
            startKoin {
                modules(performanceModule)
            }
        }
    }

    @Test
    fun `test getWalletPerformance success`() = runTest {

        val filePath = "/wallet.csv"

        val walletPerformance = PerformanceController().getWalletPerformance(filePath)

        assertEquals(
            WalletPerformance(
                totalValue = 16984.619452250183,
                Asset("", "BTC", 37870.5058, 1.5051283742084405, 0.12345, 56999.97282520531),
                Asset("", "ETH", 2004.9774, 1.0135473011095806, 4.89532, 2032.1394325557042)
            ), walletPerformance
        )
    }

    @Test
    fun `test getWalletPerformance with wrong filename`() = runTest {

        val filePath = "testFilePath"

        assertFailsWith<FileNotFoundException>(
            message = "Wallet file not found: $filePath",
            block = { PerformanceController().getWalletPerformance(filePath) }
        )
    }

}
