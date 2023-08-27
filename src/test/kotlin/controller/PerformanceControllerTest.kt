package genesischallenge

import genesischallenge.Controller.getWalletPerformance
import genesischallenge.domain.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PerformanceControllerTest {

    @Test
    fun `test getWalletPerformance success`() = runTest {
        val filePath = "/wallet.csv"

        val walletPerformance = getWalletPerformance(filePath)

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
            block = { getWalletPerformance(filePath) }
        )
    }
}
