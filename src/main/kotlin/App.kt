package genesischallenge

import genesischallenge.controller.PerformanceController
import genesischallenge.data.repository.AssetsRepository
import genesischallenge.data.repository.AssetsRepositoryImpl
import genesischallenge.data.repository.WalletRepository
import genesischallenge.data.repository.WalletRepositoryImpl
import genesischallenge.utils.roundTo2Decimal
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

val performanceModule = module {
    single<WalletRepository> { WalletRepositoryImpl() }
    single<AssetsRepository> { AssetsRepositoryImpl() }
}

fun main() {

    startKoin {
        modules(performanceModule)
    }
    runBlocking {
        try {
            val walletPerformance = PerformanceController().getWalletPerformance("/wallet.csv")

            println("total=${walletPerformance.totalValue.roundTo2Decimal()},best_asset=${walletPerformance.bestPerformingAsset.symbol},best_performance=${walletPerformance.bestPerformingAsset.performance.roundTo2Decimal()},worst_asset=${walletPerformance.worstPerformingAsset.symbol},worst_performance=${walletPerformance.worstPerformingAsset.performance.roundTo2Decimal()}")
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
        }
    }
}


