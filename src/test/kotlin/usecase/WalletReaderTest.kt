package usecase

import genesischallenge.data.repository.WalletRepository
import genesischallenge.data.repository.WalletRepositoryImpl
import genesischallenge.usecase.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import java.io.FileNotFoundException
import kotlin.test.assertFailsWith

class WalletReaderTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun setup() {
            val performanceModule = module {
                single<WalletRepository> { WalletRepositoryImpl() }
            }
            GlobalContext.startKoin {
                modules(performanceModule)
            }
        }
    }

    @Test
    fun `test readWallet success`() = runTest {

        val filePath = "/wallet.csv"
        val assets = WalletReader().readWallet(filePath)
        Assert.assertEquals(2, assets.size)
        Assert.assertEquals("BTC", assets.first().symbol)
        Assert.assertEquals("ETH", assets.last().symbol)
    }

    @Test
    fun `test readWallet with wrong filename`() = runTest {

        val filePath = "testFilePath"

        assertFailsWith<FileNotFoundException>(
            message = "Wallet file not found: $filePath",
            block = { WalletReader().readWallet(filePath) }
        )
    }
}
