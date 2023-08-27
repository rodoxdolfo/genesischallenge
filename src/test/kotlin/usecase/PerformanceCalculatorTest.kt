package genesischallenge

import genesischallenge.domain.*
import genesischallenge.utils.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceCalculatorTest {
    @Test
    fun `test calculatePerformance with 2 assets`() = runTest {
        val assets: List<Asset> = listOf(
            Asset("", "BTC", 37870.5058, 1.5051283742084405, 0.12345, 56999.97282520531),
            Asset("", "ETH", 2004.9774, 1.0135473011095806, 4.89532, 2032.1394325557042)
        )
        val walletPerformance = calculatePerformance(assets)

        assertEquals(
            WalletPerformance(
                totalValue = 16984.619452250183,
                Asset("", "BTC", 37870.5058, 1.5051283742084405, 0.12345, 56999.97282520531),
                Asset("", "ETH", 2004.9774, 1.0135473011095806, 4.89532, 2032.1394325557042)
            ), walletPerformance
        )
    }

    @Test
    fun `test calculatePerformance with 16 assets`() = runTest {
        val assets: List<Asset> = listOf(
            Asset("", "BTC", 37870.5058, 0.0, 0.12345, 56999.97282520531),
            Asset("", "ETH", 2004.9774, 0.0, 4.89532, 2000.1394325557042),
            Asset("", "BTC", 1.0, 0.0, 0.1, 1.0),
            Asset("", "ETH", 1.0, 0.0, 0.1, 1.0),
            Asset("", "BTC", 1.0, 0.0, 0.1, 1.0),
            Asset("", "ETH", 1.0, 0.0, 0.1, 1.0),
            Asset("", "BTC", 1.0, 0.0, 0.1, 1.0),
            Asset("", "ETH", 1.0, 0.0, 0.1, 1.0),
            Asset("", "BTC", 1.0, 0.0, 0.1, 1.0),
            Asset("", "ETH", 1.0, 0.0, 0.1, 1.0),
            Asset("", "BTC", 1.0, 0.0, 0.1, 1.0),
            Asset("", "ETH", 1.0, 0.0, 0.1, 1.0),
            Asset("", "BTC", 1.0, 0.0, 0.1, 1.0),
            Asset("", "ETH", 1.0, 0.0, 0.1, 1.0),
            Asset("", "BTC", 1.0, 0.0, 0.1, 1.0),
            Asset("", "ETH", 1.0, 0.0, 0.1, 1.0)
        )
        val walletPerformance = calculatePerformance(assets)

        assertEquals(
            WalletPerformance(
                totalValue = 16829.369212250167,
                Asset("", "BTC", 37870.5058, 1.5051283742084405, 0.12345, 56999.97282520531),
                Asset("", "ETH", 2004.9774, 0.9975870214575506, 4.89532, 2000.1394325557042)
            ), walletPerformance
        )
    }
}


